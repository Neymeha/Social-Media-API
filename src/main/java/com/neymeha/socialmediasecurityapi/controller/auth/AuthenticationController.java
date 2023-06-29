package com.neymeha.socialmediasecurityapi.controller.auth;

import com.neymeha.socialmediasecurityapi.service.auth.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// рест контроллер для аутентификации и регистрации пользователей
@RestController
@RequestMapping("/social/media/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl service; // добавили зависимость от нашего сервиса с реализацией логики ответов на запросы

    @PostMapping("/registration") // endpoint для регистрации запрашивает поля из обьекта запроса на регистрацию
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return new ResponseEntity(service.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/authentication") // endpoint для аутентификации запрашивает поля из обьекта запроса на аутентификацию
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return new ResponseEntity(service.authenticate(request), HttpStatus.OK);
    }

    @PutMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshRequest request){
        return new ResponseEntity(service.refreshToken(request), HttpStatus.CREATED);
    }
}
