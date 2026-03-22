package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для работы с изображениями.
 * Содержит методы для сохранения, получения и удаления файлов изображений.
 */
public interface ImageService {

    /**
     * Сохраняет изображение в файловой системе.
     *
     * @param image файл изображения
     * @return имя сохранённого файла
     */
    String saveImage(MultipartFile image);

    /**
     * Возвращает изображение по имени файла.
     *
     * @param fileName имя файла изображения
     * @return массив байтов изображения
     */
    byte[] getImage(String fileName);

    /**
     * Удаляет изображение по имени файла.
     *
     * @param fileName имя файла изображения
     */
    void deleteImage(String fileName);
}