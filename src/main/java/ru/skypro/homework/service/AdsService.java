package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

/**
 * Сервис для работы с объявлениями.
 * Содержит методы для получения, создания, изменения и удаления объявлений,
 * а также для работы с изображениями объявлений.
 */
public interface AdsService {

    /**
     * Возвращает список всех объявлений.
     *
     * @return объект со списком объявлений и их количеством
     */
    Ads getAllAds();

    /**
     * Возвращает полную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return полная информация об объявлении
     */
    ExtendedAd getAdById(Integer id);

    /**
     * Возвращает список объявлений текущего пользователя.
     *
     * @param email email пользователя
     * @return объект со списком объявлений пользователя и их количеством
     */
    Ads getAdsMe(String email);

    /**
     * Создаёт новое объявление.
     *
     * @param email email пользователя
     * @param createOrUpdateAd данные для создания объявления
     * @param image файл изображения объявления
     * @return созданное объявление
     */
    Ad addAd(String email, CreateOrUpdateAd createOrUpdateAd, MultipartFile image);

    /**
     * Обновляет объявление по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @param email email пользователя
     * @param createOrUpdateAd данные для обновления объявления
     * @return обновлённое объявление
     */
    Ad updateAd(Integer adId, String email, CreateOrUpdateAd createOrUpdateAd);

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @param email email пользователя
     */
    void deleteAd(Integer adId, String email);

    /**
     * Обновляет изображение объявления.
     *
     * @param id идентификатор объявления
     * @param email email пользователя
     * @param image файл нового изображения
     */
    void updateImage(Integer id, String email, MultipartFile image);

    /**
     * Возвращает изображение объявления по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return массив байтов изображения
     */
    byte[] getAdImage(Integer id);
}