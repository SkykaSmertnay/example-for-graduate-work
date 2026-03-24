package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdsService {

    Ads getAllAds();

    ExtendedAd getAdById(Integer id);

    Ads getAdsMe(String email);

    Ad addAd(String email, CreateOrUpdateAd createOrUpdateAd, MultipartFile image);

    Ad updateAd(Integer adId, String email, CreateOrUpdateAd createOrUpdateAd);

    void deleteAd(Integer adId, String email);

    void updateImage(Integer adId, String email, MultipartFile image);
}