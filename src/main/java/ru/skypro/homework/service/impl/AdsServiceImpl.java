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
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;

import java.util.List;
import ru.skypro.homework.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private static final String AD_NOT_FOUND_MESSAGE = "Ad not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String FORBIDDEN_MESSAGE = "Access denied";

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        return adMapper.toAdsDto(adEntities);
    }

    @Override
    public ExtendedAd getAdById(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));
        return adMapper.toExtendedDto(adEntity);
    }

    @Override
    public Ads getAdsMe(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        List<AdEntity> adEntities = adRepository.findAllByAuthorId(userEntity.getId());
        return adMapper.toAdsDto(adEntities);
    }

    @Override
    public Ad addAd(String email, CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        AdEntity adEntity = adMapper.createToEntity(createOrUpdateAd);
        adEntity.setAuthor(userEntity);
        adEntity.setImage(image.getOriginalFilename());

        AdEntity savedAd = adRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

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

    @Override
    public void deleteAd(Integer adId, String email) {
        UserEntity currentUser = getUserByEmail(email);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        checkAdAccess(adEntity, currentUser);

        adRepository.delete(adEntity);
    }

    @Override
    public void updateImage(Integer adId, String email, MultipartFile image) {
        UserEntity currentUser = getUserByEmail(email);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        checkAdAccess(adEntity, currentUser);

        adEntity.setImage(image.getOriginalFilename());
        adRepository.save(adEntity);
    }

    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    private void checkAdAccess(AdEntity adEntity, UserEntity currentUser) {
        boolean isAuthor = adEntity.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException(FORBIDDEN_MESSAGE);
        }
    }
}