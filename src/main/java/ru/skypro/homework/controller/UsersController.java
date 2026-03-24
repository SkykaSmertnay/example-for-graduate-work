package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UsersService;

import javax.validation.Valid;

/**
 * Контроллер для работы с пользователями.
 * Обрабатывает запросы на получение и изменение профиля,
 * смену пароля и работу с изображением пользователя.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    /**
     * Изменяет пароль авторизованного пользователя.
     *
     * @param newPassword данные для смены пароля
     * @param authentication данные авторизованного пользователя
     * @return пустой ответ
     */
    @Operation(summary = "Обновление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль обновлён"),
            @ApiResponse(responseCode = "401", description = "Неавторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPassword newPassword,
                                            Authentication authentication) {
        usersService.setPassword(authentication.getName(), newPassword);
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает профиль авторизованного пользователя.
     *
     * @param authentication данные авторизованного пользователя
     * @return данные пользователя
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        return ResponseEntity.ok(usersService.getUser(authentication.getName()));
    }

    /**
     * Обновляет профиль авторизованного пользователя.
     *
     * @param updateUser данные для обновления профиля
     * @param authentication данные авторизованного пользователя
     * @return обновлённые данные пользователя
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация обновлена"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@Valid @RequestBody UpdateUser updateUser,
                                                 Authentication authentication) {
        return ResponseEntity.ok(usersService.updateUser(authentication.getName(), updateUser));
    }

    /**
     * Обновляет аватар авторизованного пользователя.
     *
     * @param image файл нового изображения
     * @param authentication данные авторизованного пользователя
     * @return пустой ответ
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аватар обновлён"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image,
                                                Authentication authentication) {
        usersService.updateUserImage(authentication.getName(), image);
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает изображение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return массив байтов изображения
     */
    @GetMapping(value = "/image/{id}", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/*"
    })
    public ResponseEntity<byte[]> getUserImage(@PathVariable Integer id) {
        return ResponseEntity.ok(usersService.getUserImage(id));
    }
}