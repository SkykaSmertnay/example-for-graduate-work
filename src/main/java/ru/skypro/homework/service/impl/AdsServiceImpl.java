package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.util.List;

/**
 * Реализация сервиса для работы с объявлениями.
 * Содержит бизнес-логику получения, создания, изменения и удаления объявлений,
 * а также работы с изображениями объявлений.
 */
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private static final String AD_NOT_FOUND_MESSAGE = "Ad not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String FORBIDDEN_MESSAGE = "Access denied";

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    /**
     * Возвращает список всех объявлений.
     *
     * @return объект со списком объявлений и их количеством
     */
    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        return adMapper.toAdsDto(adEntities);
    }

    /**
     * Возвращает полную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return полная информация об объявлении
     */
    @Override
    public ExtendedAd getAdById(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));
        return adMapper.toExtendedDto(adEntity);
    }

    /**
     * Возвращает список объявлений текущего пользователя.
     *
     * @param email email пользователя
     * @return объект со списком объявлений пользователя и их количеством
     */
    @Override
    public Ads getAdsMe(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        List<AdEntity> adEntities = adRepository.findAllByAuthorId(userEntity.getId());
        return adMapper.toAdsDto(adEntities);
    }

    /**
     * Создаёт новое объявление и сохраняет его изображение.
     *
     * @param email email пользователя
     * @param createOrUpdateAd данные для создания объявления
     * @param image файл изображения объявления
     * @return созданное объявление
     */
    @Override
    public Ad addAd(String email, CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        AdEntity adEntity = adMapper.createToEntity(createOrUpdateAd);
        adEntity.setAuthor(userEntity);

        String fileName = imageService.saveImage(image);
        adEntity.setImage(fileName);

        AdEntity savedAd = adRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

    /**
     * Обновляет объявление по его идентификатору.
     * Изменение доступно только автору объявления или администратору.
     *
     * @param adId идентификатор объявления
     * @param email email пользователя
     * @param createOrUpdateAd данные для обновления объявления
     * @return обновлённое объявление
     */
    @Override
    public Ad updateAd(Integer adId, String email, CreateOrUpdateAd createOrUpdateAd) {
        UserEntity currentUser = getUserByEmail(email);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        checkAdAccess(adEntity, currentUser);

        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        adEntity.setPrice(createOrUpdateAd.getPrice());

        AdEntity savedAd = adRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

    /**
     * Удаляет объявление по его идентификатору.
     * Перед удалением удаляет связанное изображение, если оно существует.
     * Удаление доступно только автору объявления или администратору.
     *
     * @param adId идентификатор объявления
     * @param email email пользователя
     */
    @Override
    public void deleteAd(Integer adId, String email) {
        UserEntity currentUser = getUserByEmail(email);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        checkAdAccess(adEntity, currentUser);

        if (adEntity.getImage() != null && !adEntity.getImage().isBlank()) {
            imageService.deleteImage(adEntity.getImage());
        }

        adRepository.delete(adEntity);
    }

    /**
     * Обновляет изображение объявления.
     * Старое изображение удаляется перед сохранением нового.
     * Обновление доступно только автору объявления или администратору.
     *
     * @param id идентификатор объявления
     * @param email email пользователя
     * @param image файл нового изображения
     */
    @Override
    public void updateImage(Integer id, String email, MultipartFile image) {
        UserEntity currentUser = getUserByEmail(email);

        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        checkAdAccess(adEntity, currentUser);

        if (adEntity.getImage() != null && !adEntity.getImage().isBlank()) {
            imageService.deleteImage(adEntity.getImage());
        }

        String fileName = imageService.saveImage(image);
        adEntity.setImage(fileName);

        adRepository.save(adEntity);
    }

    /**
     * Возвращает изображение объявления по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return массив байтов изображения
     */
    @Override
    public byte[] getAdImage(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        if (adEntity.getImage() == null || adEntity.getImage().isBlank()) {
            throw new NotFoundException("Image not found");
        }

        return imageService.getImage(adEntity.getImage());
    }

    /**
     * Возвращает пользователя по email.
     *
     * @param email email пользователя
     * @return сущность пользователя
     */
    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    /**
     * Проверяет, имеет ли пользователь право изменять объявление.
     * Доступ разрешён автору объявления или пользователю с ролью ADMIN.
     *
     * @param adEntity объявление
     * @param currentUser текущий пользователь
     */
    private void checkAdAccess(AdEntity adEntity, UserEntity currentUser) {
        boolean isAuthor = adEntity.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException(FORBIDDEN_MESSAGE);
        }
    }
}