package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Маппер для преобразования сущности объявления в DTO и обратно.
 */
@Mapper(config = MapStructConfig.class)
public interface AdMapper {

    /**
     * Преобразует DTO создания/обновления объявления в сущность объявления.
     *
     * @param createOrUpdateAd DTO с данными объявления
     * @return сущность объявления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    AdEntity createToEntity(CreateOrUpdateAd createOrUpdateAd);

    /**
     * Преобразует сущность объявления в краткое DTO объявления.
     *
     * @param adEntity сущность объявления
     * @return DTO объявления
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", expression = "java(adEntity.getImage() != null && !adEntity.getImage().isBlank() ? \"/ads/image/\" + adEntity.getId() : null)")
    Ad toDto(AdEntity adEntity);

    /**
     * Преобразует список сущностей объявлений в список DTO.
     *
     * @param adEntities список сущностей объявлений
     * @return список DTO объявлений
     */
    List<Ad> toDtoList(List<AdEntity> adEntities);

    /**
     * Формирует DTO со списком объявлений и их количеством.
     *
     * @param adEntities список сущностей объявлений
     * @return DTO со списком объявлений
     */
    default Ads toAdsDto(List<AdEntity> adEntities) {
        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(toDtoList(adEntities));
        return ads;
    }

    /**
     * Преобразует сущность объявления в расширенное DTO объявления.
     *
     * @param adEntity сущность объявления
     * @return расширенное DTO объявления
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", expression = "java(adEntity.getImage() != null && !adEntity.getImage().isBlank() ? \"/ads/image/\" + adEntity.getId() : null)")
    ExtendedAd toExtendedDto(AdEntity adEntity);
}