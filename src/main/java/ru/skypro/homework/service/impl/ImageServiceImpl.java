package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Реализация сервиса для работы с изображениями.
 * Содержит логику сохранения, получения и удаления файлов изображений
 * в файловой системе.
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${app.image.dir}")
    private String imageDir;

    /**
     * Сохраняет изображение в файловой системе.
     * При сохранении генерируется уникальное имя файла.
     *
     * @param image файл изображения
     * @return имя сохранённого файла
     */
    @Override
    public String saveImage(MultipartFile image) {
        try {
            Path uploadPath = Paths.get(imageDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = image.getOriginalFilename();
            String extension = getExtension(originalFilename);
            String fileName = UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, image.getBytes());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Возвращает изображение по имени файла.
     *
     * @param fileName имя файла изображения
     * @return массив байтов изображения
     */
    @Override
    public byte[] getImage(String fileName) {
        try {
            Path filePath = Paths.get(imageDir).resolve(fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        }
    }

    /**
     * Удаляет изображение по имени файла.
     *
     * @param fileName имя файла изображения
     */
    @Override
    public void deleteImage(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }

        try {
            Path filePath = Paths.get(imageDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    /**
     * Возвращает расширение файла.
     *
     * @param fileName имя файла
     * @return расширение файла или пустая строка, если расширение отсутствует
     */
    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}