package com.neymeha.socialmediasecurityapi.config;

import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// класс для бинов нашего приложения
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository; // подгружаем зависимость для поиска через login/username - email нашего User из бд

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository
                .findByEmail(username) // поскольку возвращает Optional и мы можем не найти User стоит добавить выброс исключения
                .orElseThrow(()->new UserNotFoundException("User not found!", HttpStatus.CONFLICT));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){ // это такой data access object который отвечает за доставку userdetails, декодировку паролей и тд и тп
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // мы используем Data Access Object провайдер
        authProvider.setUserDetailsService(userDetailsService()); // ему нужно задать UserDetailsService потому что их может быть не 1 в проэкте
        authProvider.setPasswordEncoder(passwordEncoder()); // нужно так же задать ему логику кодироки пароля
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // бин для кодировки паролей
        return new BCryptPasswordEncoder();
    } // добавляем бин для кодировок

    @Bean // данный менеджер имеет методы которые помогают в аутентификации, например просто по паролю и логину, собственно управляет аутентификацией
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager(); // дефолтная имплиментация спринга
    }
}
