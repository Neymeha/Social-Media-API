package com.neymeha.socialmediasecurityapi.service.auth;

import com.neymeha.socialmediasecurityapi.controller.auth.AuthenticationRequest;
import com.neymeha.socialmediasecurityapi.controller.auth.AuthenticationResponse;
import com.neymeha.socialmediasecurityapi.controller.auth.RefreshRequest;
import com.neymeha.socialmediasecurityapi.controller.auth.RegisterRequest;
import com.neymeha.socialmediasecurityapi.customexceptions.jwt.JwtException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.InappropriateEmailException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequiredFieldsAreNotFilledException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserAlreadyExistException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.entity.Role;
import com.neymeha.socialmediasecurityapi.entity.User;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// класс для аутентификации
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository; // зависимость для круд операций
    private final PasswordEncoder passwordEncoder; // зависимость для кодировки
    private final JwtService jwtService; // зависимость для манипуляций с токеном
    private final AuthenticationManager authenticationManager; // зависимость для дефолтного менеджера аутентификации спринга

    @Override
    public AuthenticationResponse register(RegisterRequest request) { // метод для ответов на запросы регистрации пользователя

        if (request.getEmail().isEmpty() || request.getEmail()==null){
            throw new RequiredFieldsAreNotFilledException("Email could not be empty or null!", HttpStatus.BAD_REQUEST);
        } else if (request.getPassword().isEmpty() || request.getPassword()==null){
            throw new RequiredFieldsAreNotFilledException("Password could not be empty or null!", HttpStatus.BAD_REQUEST);
        } else if (request.getName().isEmpty() || request.getName()==null){
            throw  new RequiredFieldsAreNotFilledException("Name could not be empty or null!", HttpStatus.BAD_REQUEST);
        }

        if (!emailPatternMatches(request.getEmail())){
            throw new InappropriateEmailException("Email should look like this \"username@domain.xxx\"!", HttpStatus.BAD_REQUEST);
        }

        var email = userRepository.findByEmail(request.getEmail());
        if (email.isPresent()){
            throw new UserAlreadyExistException("User already exists!", HttpStatus.CONFLICT);
        }
        var user = User.builder() // используем билдер для генерации
                .name(request.getName()) // из запроса пользователя достаем имя
                .email(request.getEmail()) // мыло
                .password(passwordEncoder.encode(request.getPassword())) // декодируем пароль
                .role(Role.USER) // даем роль
                .build(); // создаем обьект
        userRepository.save(user); // сохраняем в базу данных нашего пользователя
        Map<String, Object> userId = new HashMap<>(); // добавляем токену юзер айди для фронта
        userId.put("userId", user.getUserId());
        var jwtAccessToken = jwtService.generateJwtAccessToken(userId,user); // генерируем токен на основе полученных данных из запроса
        var jwtRefreshToken = jwtService.generateJwtRefreshToken(userId,user); // генерируем рефреш токен
        return AuthenticationResponse.builder()
                .jwtAccessToken(jwtAccessToken)
                .jwtRefreshToken(jwtRefreshToken)
                .build(); // возвращаем наш обьект ответа аутентификации прикладывая токен
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) { // метод для ответов на запросы по аутентификации уже зарегестрированных пользователей

        if (request.getEmail().isEmpty() || request.getEmail()==null){
            throw new RequiredFieldsAreNotFilledException("Email could not be empty or null!", HttpStatus.BAD_REQUEST);
        } else if (request.getPassword().isEmpty() || request.getPassword()==null){
            throw new RequiredFieldsAreNotFilledException("Password could not be empty or null!", HttpStatus.BAD_REQUEST);
        }

        authenticationManager.authenticate( // используем метод для аутентификации по логину и паролю от спринга
                new UsernamePasswordAuthenticationToken( // достаем лог пасс из запроса и вкладываем в реализацию менеджера
                        request.getEmail(),
                        request.getPassword()
                )
        ); // ниже находим пользователя в БД по уникальному эмейлу, если не найден выбрасываем Exception
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        Map<String, Object> userId = new HashMap<>(); // добавляем токену юзер айди для фронта
        userId.put("userId", user.getUserId());
        var jwtAccessToken = jwtService.generateJwtAccessToken(userId,user); // генерируем токен на основе полученных данных из запроса
        var jwtRefreshToken = jwtService.generateJwtRefreshToken(userId,user); // генерируем рефреш токен
        return AuthenticationResponse.builder()
                .jwtAccessToken(jwtAccessToken)
                .jwtRefreshToken(jwtRefreshToken)
                .build(); // возвращаем обьект ответа по аутентификации прикладывая токен
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) {

        if (refreshRequest.getRefreshToken().isEmpty() || refreshRequest.getRefreshToken()==null){
            throw new RequestException("Refresh token could be empty or null!", HttpStatus.BAD_REQUEST);
        }

        // немного не нравится мне такая проверка ниже, было бы хорошо искать в базе данных юзер по какому то ключу который в токене не содержится который передается с фронта а потом уже проверять на соответствие например передача айди от фронта, но для этого фронт должен будет хранить айдишники и привязывать их к чему то
        var userEmail = jwtService.extractUsername(refreshRequest.getRefreshToken());
        var user = userRepository.findByEmail(userEmail).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));

        if (!jwtService.isTokenValid(refreshRequest.getRefreshToken(), user)) {
            throw new JwtException("Refresh token is invalid!", HttpStatus.NOT_ACCEPTABLE);
        }

        Map<String, Object> userId = new HashMap<>(); // добавляем токену юзер айди для фронта
        userId.put("userId", user.getUserId());
        var jwtAccessToken = jwtService.generateJwtAccessToken(userId,user); // генерируем токен на основе полученных данных из запроса
        var jwtRefreshToken = jwtService.generateJwtRefreshToken(userId,user); // генерируем рефреш токен
        return AuthenticationResponse.builder()
                .jwtAccessToken(jwtAccessToken)
                .jwtRefreshToken(jwtRefreshToken)
                .build();
    }

    private boolean emailPatternMatches(String emailAddress) { // проверка эмейла на соответствие паттерну/стандарту
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
