package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация Spring Security.
 * Определяет правила доступа к endpoint'ам приложения,
 * настраивает HTTP Basic аутентификацию и бины безопасности.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    /**
     * Список публичных endpoint'ов, доступных без авторизации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register"
    };

    private final UserDetailsService userDetailsService;

    /**
     * Настраивает цепочку фильтров безопасности и правила доступа к endpoint'ам.
     *
     * @param http объект конфигурации безопасности
     * @return цепочка фильтров безопасности
     * @throws Exception если произошла ошибка настройки безопасности
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers(HttpMethod.GET, "/ads").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/ads/me").hasAnyRole("USER", "ADMIN")
                        .mvcMatchers(HttpMethod.GET, "/ads/*/comments").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/ads/*").permitAll()
                        .mvcMatchers("/users/**").hasAnyRole("USER", "ADMIN")
                        .mvcMatchers(HttpMethod.POST, "/ads/**").hasAnyRole("USER", "ADMIN")
                        .mvcMatchers(HttpMethod.PATCH, "/ads/**").hasAnyRole("USER", "ADMIN")
                        .mvcMatchers(HttpMethod.DELETE, "/ads/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .cors()
                .and()
                .httpBasic(withDefaults());

        return http.build();
    }

    /**
     * Создаёт провайдер аутентификации на основе {@link UserDetailsService}
     * и кодировщика паролей.
     *
     * @param userDetailsService сервис загрузки пользователей
     * @param passwordEncoder кодировщик паролей
     * @return провайдер аутентификации
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Создаёт кодировщик паролей BCrypt.
     *
     * @return кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}