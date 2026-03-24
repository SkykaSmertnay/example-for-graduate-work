package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Сервис для авторизации и регистрации пользователей.
 */
public interface AuthService {

    /**
     * Проверяет данные пользователя для входа в систему.
     *
     * @param userName логин пользователя
     * @param password пароль пользователя
     * @return {@code true}, если авторизация успешна, иначе {@code false}
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя.
     *
     * @param register данные для регистрации пользователя
     * @return {@code true}, если регистрация успешна, иначе {@code false}
     */
    boolean register(Register register);
}