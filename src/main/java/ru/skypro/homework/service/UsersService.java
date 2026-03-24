package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

/**
 * Сервис для работы с пользователями.
 * Содержит методы для получения и обновления профиля,
 * смены пароля и работы с изображением пользователя.
 */
public interface UsersService {

    /**
     * Изменяет пароль авторизованного пользователя.
     *
     * @param email email пользователя
     * @param newPassword объект с текущим и новым паролем
     */
    void setPassword(String email, NewPassword newPassword);

    /**
     * Возвращает данные профиля пользователя по его email.
     *
     * @param email email пользователя
     * @return данные пользователя
     */
    User getUser(String email);

    /**
     * Возвращает данные пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return данные пользователя
     */
    User getUserById(Integer id);

    /**
     * Обновляет данные профиля пользователя.
     *
     * @param email email пользователя
     * @param updateUser объект с обновлёнными данными пользователя
     * @return обновлённые данные пользователя
     */
    UpdateUser updateUser(String email, UpdateUser updateUser);

    /**
     * Обновляет изображение профиля пользователя.
     *
     * @param email email пользователя
     * @param image файл изображения
     */
    void updateUserImage(String email, MultipartFile image);

    /**
     * Возвращает изображение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return массив байтов изображения
     */
    byte[] getUserImage(Integer id);
}