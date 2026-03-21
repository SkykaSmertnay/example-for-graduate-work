package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UsersService {

    void setPassword(String email, NewPassword newPassword);

    User getUser(String email);

    User getUserById(Integer id);

    UpdateUser updateUser(String email, UpdateUser updateUser);

    void updateUserImage(String email, MultipartFile image);

    byte[] getUserImage(Integer id);
}