package com.neymeha.socialmediasecurityapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// класс который свяжет(подскажет спрингу что нужно использовать) наш фильтр и все остальные настройки вместе
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // добавляем зависимость от нашего фильтра
    private final AuthenticationProvider authenticationProvider; // добавляем зависимость от провайдера дефолтной реализации спринга
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf() // отключаем аутентификацию csrf
                .disable()
                .authorizeRequests() // команда для дальнейшего списка endpoint
//                .requestMatchers("/social/media/api/v1/auth/**") // список endpoint // такой код не подошел
//                .permitAll() // разрешить доступ для списка выше // такой код не подошел
//                .requestMatchers("/social/media/api/v1/**") // такой код не подошел
                .requestMatchers("/social/media/api/v1/feed")
                .authenticated() // запрос аутентификации
                .requestMatchers("/social/media/api/v1/feed/**")
                .authenticated()
                .requestMatchers("/social/media/api/v1/posts")
                .authenticated()
                .requestMatchers("/social/media/api/v1/posts/**")
                .authenticated()
                .requestMatchers("/social/media/api/v1/messages")
                .authenticated()
                .requestMatchers("/social/media/api/v1/messages/**")
                .authenticated()
                .requestMatchers("/social/media/api/v1/status")
                .authenticated()
                .requestMatchers("/social/media/api/v1/status/**")
                .authenticated()
                .requestMatchers("/social/media/api/v1/users")
                .authenticated()
                .requestMatchers("/social/media/api/v1/users/**")
                .authenticated()
                .and()
                .sessionManagement() // поскольку мы не хотим хранить аутентификацию так как для каждого запроса она понадобися
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // по этому нужно сделать ее stateless, новая сессия для каждого нового запроса
                .and()
                .authenticationProvider(authenticationProvider) // нужно сказать спрингу какой провайдер для аутентификации мы будем использовать
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // мы хотим что бы наш фильтр спрабатывал раньше/перед тем что указан
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
