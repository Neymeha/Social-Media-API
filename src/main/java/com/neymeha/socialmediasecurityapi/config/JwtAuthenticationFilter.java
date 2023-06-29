package com.neymeha.socialmediasecurityapi.config;

import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // что бы сделать bean из класса, можно Service/Repository
@RequiredArgsConstructor // генерирует конструктор со всеми final field
public class JwtAuthenticationFilter extends OncePerRequestFilter { // класс можно создать разными способами например
                                                                    // impliment Filter (интерфейс) и описать полную реализацию
    private final JwtService jwtService; // добавляем зависимость от класса с функционалом манипуляцией токеном
    private final UserDetailsService userDetailsService; // добавляем зависимость от класса в котором есть метод для поиска Юзера по username в БД

    @Override
    protected void doFilterInternal( // запросы не должны быть null как и filterChain поэтому @NonNull
            @NonNull HttpServletRequest request, // запрос от пользователя
            @NonNull HttpServletResponse response, // ответ от приложения
            @NonNull FilterChain filterChain // паттерн содержащий цепочку других фильтров
    ) throws ServletException, IOException {
        // достаем токен
        final String authHeader = request.getHeader("Authorization"); // когда мы делаем запрос, нам нужно передать token frontend, и тут мы его достаем из header, Authorization - имя header
        final String jwtToken; // сюда положим готовый токен из authHeader
        final String userEmail; // сюда положим Логин(в нашем случае мыло) для проверки есть ли такой пользователь.
        // далее реализация кода проверки на наличие токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // если токена нет, или токен не начинается с ключевого слова (для чистого токена)
            filterChain.doFilter(request, response); // проверка не пройдена - передаем дальше
            return; // и закрываем метод так как дальнейший код для этого случая не подходит
        }
        jwtToken = authHeader.substring(7); // токен есть в наличии и он верный, достаем его (он начинается с 7ой позиции, после "Bearer ")
        userEmail = jwtService.extractUsername(jwtToken); // достаем логин (эмейл) из токена с помощью класса с функционалом для манипулирования токеном
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){ // если мы получили эмейл и пользователь еще не прошел аутентификацию
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // загружаем нашего пользователя из БД (можно исп вместо UserDetails - User)
            if (jwtService.isTokenValid(jwtToken,userDetails)){ // проверяем на пригодность токен
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( // этот обьект нужен спрингу что бы обновить security context
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // наполняем дополнительными details наш authToken взяв их из запроса пользователя
                SecurityContextHolder.getContext().setAuthentication(authToken); // обновляем
            }
        }
        filterChain.doFilter(request, response); // передаем дальнейшим фильтрам
    }
}
