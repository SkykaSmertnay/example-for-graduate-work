package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UsersService;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String INVALID_PASSWORD_MESSAGE = "Current password is incorrect";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public void setPassword(String email, NewPassword newPassword) {
        UserEntity userEntity = getUserEntityByEmail(email);

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException(INVALID_PASSWORD_MESSAGE);
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public User getUser(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);
        return userMapper.toDto(userEntity);
    }

    @Override
    public User getUserById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        return userMapper.toDto(userEntity);
    }

    @Override
    public UpdateUser updateUser(String email, UpdateUser updateUser) {
        UserEntity userEntity = getUserEntityByEmail(email);
        userMapper.updateUserToEntity(updateUser, userEntity);
        userRepository.save(userEntity);
        return updateUser;
    }

    @Override
    public void updateUserImage(String email, MultipartFile image) {
        UserEntity userEntity = getUserEntityByEmail(email);

        if (userEntity.getImage() != null && !userEntity.getImage().isBlank()) {
            imageService.deleteImage(userEntity.getImage());
        }

        String fileName = imageService.saveImage(image);
        userEntity.setImage(fileName);

        userRepository.save(userEntity);
    }

    @Override
    public byte[] getUserImage(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        if (userEntity.getImage() == null || userEntity.getImage().isBlank()) {
            throw new NotFoundException("Image not found");
        }

        return imageService.getImage(userEntity.getImage());
    }

    private UserEntity getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }
}