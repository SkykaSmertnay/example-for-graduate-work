package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса для авторизации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Проверяет данные пользователя для входа в систему.
     *
     * @param userName логин пользователя
     * @param password пароль пользователя
     * @return {@code true}, если авторизация успешна, иначе {@code false}
     */
    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Регистрирует нового пользователя.
     * Если пользователь с таким email уже существует, регистрация не выполняется.
     *
     * @param register данные для регистрации пользователя
     * @return {@code true}, если регистрация успешна, иначе {@code false}
     */
    @Override
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }

        UserEntity userEntity = userMapper.registerToEntity(register);
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(userEntity);

        return true;
    }
}