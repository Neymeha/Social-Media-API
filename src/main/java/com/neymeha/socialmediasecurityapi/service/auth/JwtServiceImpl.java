package com.neymeha.socialmediasecurityapi.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// класс для манипуляций/валидации jwt token, нужны зависимости: jjwt-api, jjwt-impl, jjwt-jackson
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    // generated with Hex at https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.expiration}")
    private long jwtAccessExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    private final HttpServletRequest request;

    @Override
    public String extractJwtTokenFromRequestHeader(){
        return request.getHeader("Authorization").substring(7);
    }

    @Override
    public String generateJwtAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildJwtToken(extraClaims, userDetails, jwtAccessExpiration);
    }

    @Override
    public String generateJwtAccessToken(UserDetails userDetails){ // метод для генерации токена без дополнительных claims
        return buildJwtToken(new HashMap<>(),userDetails, jwtAccessExpiration);
    }

    @Override
    public String generateJwtRefreshToken(UserDetails userDetails) { // метод для генерации рефреш токена
        return buildJwtToken(new HashMap<>(), userDetails, jwtRefreshExpiration);
    }

    @Override
    public String generateJwtRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildJwtToken(extraClaims, userDetails, jwtAccessExpiration);
    }

    @Override
    public boolean isTokenValid(String jwtToken, UserDetails userDetails){ // валидация токена
        final String username = extractUsername(jwtToken); // получаем login/username - email в нашем случае
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken)); // проверка на совпадение юзернейма из токена с юзернеймом из энтити а так же токен должен быть с не истекшим сроком
    }

    @Override
    public String extractUsername(String jwtToken) { // метод для получения login(email)/username из токена
        return extractClaim(jwtToken, Claims::getSubject); // getSubject в нашем случае вернет email
    }

    public String extractUsernameFromAuthJwt(){
        return extractUsername(extractJwtTokenFromRequestHeader());
    }

    @Override
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) { // метод для получения конкретного claim
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private String buildJwtToken( // метод для cоздания токена с доп. claims
                                  Map<String, Object> extraClaims,
                                  UserDetails userDetails,
                                  long expiration
    ) {
        return Jwts
                .builder() // вызываем билдер для создания токена
                .setClaims(extraClaims)  // добавляем доп claims
                .setSubject(userDetails.getUsername())  // добавляем login/username - email в subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // добавляем дату создания
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // добавляем дату истечения срока действия
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)  // добавляем "подпись-ключ" и алгоритм шифрования
                .compact();  // создаем токен
    }

    private Date extractExpiration(String jwtToken) { // получаем дату истечение срока действия из токена
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    @Override
    public boolean isTokenExpired(String jwtToken) { // проверяем токен на истечение срока действия
        return extractExpiration(jwtToken).before(new Date());
    }

    @Override
    public Claims extractAllClaims(String jwtToken){ // метод для получения всех claims из token, с помощью класса из dependancy
        return Jwts //
                .parserBuilder()
                .setSigningKey(getSignedKey()) // для того что бы чтото делать с токеном нужно ввести "подпись-ключь"
                .build() // это @Builder так что нужно исп build() что бы получить обьект
                .parseClaimsJws(jwtToken) // парсим токен
                .getBody(); // получаем все claims этого токена
    }

    private Key getSignedKey() { // метод для получение "подписи-ключа"
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // декодируем секретный ключь
        return Keys.hmacShaKeyFor(keyBytes); // алгоритм для создания подписи-ключа
    }
}
