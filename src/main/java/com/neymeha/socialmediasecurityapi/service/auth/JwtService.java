package com.neymeha.socialmediasecurityapi.service.auth;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateJwtAccessToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateJwtAccessToken(UserDetails userDetails);
    String generateJwtRefreshToken(UserDetails userDetails);
    boolean isTokenValid(String jwtToken, UserDetails userDetails);
    String extractUsername(String jwtToken);
    <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver);
    boolean isTokenExpired(String jwtToken);
    Claims extractAllClaims(String jwtToken);
    String extractJwtTokenFromRequestHeader();
    String extractUsernameFromAuthJwt();
    String generateJwtRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
