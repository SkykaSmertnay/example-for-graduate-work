package ru.skypro.homework.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для добавления CORS-заголовка,
 * разрешающего передачу credentials в запросах.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Добавляет заголовок {@code Access-Control-Allow-Credentials}
     * и передаёт запрос дальше по цепочке фильтров.
     *
     * @param httpServletRequest входящий HTTP-запрос
     * @param httpServletResponse исходящий HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если произошла ошибка сервлета
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}