package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

import javax.validation.Valid;

/**
 * Контроллер для работы с объявлениями.
 * Обрабатывает запросы на получение, создание, изменение и удаление объявлений,
 * а также на получение и обновление изображений объявлений.
 */
@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;
    private final ObjectMapper objectMapper;

    /**
     * Возвращает список всех объявлений.
     *
     * @return список объявлений
     */
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявления получены")
    })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    /**
     * Создаёт новое объявление.
     *
     * @param properties строка JSON с данными объявления
     * @param image файл изображения объявления
     * @param authentication данные авторизованного пользователя
     * @return созданное объявление
     * @throws Exception если произошла ошибка при преобразовании JSON в DTO
     */
    @Operation(summary = "Добавление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление создано"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(@RequestPart("properties") String properties,
                                    @RequestPart("image") MultipartFile image,
                                    Authentication authentication) throws Exception {
        CreateOrUpdateAd createOrUpdateAd = objectMapper.readValue(properties, CreateOrUpdateAd.class);
        Ad ad = adsService.addAd(authentication.getName(), createOrUpdateAd, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    /**
     * Возвращает полную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return полная информация об объявлении
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getAdById(id));
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     * @param authentication данные авторизованного пользователя
     * @return пустой ответ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id,
                                         Authentication authentication) {
        adsService.deleteAd(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     * @param createOrUpdateAd данные для обновления объявления
     * @param authentication данные авторизованного пользователя
     * @return обновлённое объявление
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @Valid @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                        Authentication authentication) {
        return ResponseEntity.ok(
                adsService.updateAd(id, authentication.getName(), createOrUpdateAd)
        );
    }

    /**
     * Возвращает список объявлений текущего пользователя.
     *
     * @param authentication данные авторизованного пользователя
     * @return список объявлений пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAdsMe(authentication.getName()));
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id идентификатор объявления
     * @param image файл нового изображения
     * @param authentication данные авторизованного пользователя
     * @return пустой ответ
     */
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateImage(@PathVariable Integer id,
                                            @RequestPart("image") MultipartFile image,
                                            Authentication authentication) {
        adsService.updateImage(id, authentication.getName(), image);
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает изображение объявления по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return массив байтов изображения
     */
    @GetMapping(value = "/image/{id}", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/*"
    })
    public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getAdImage(id));
    }
}