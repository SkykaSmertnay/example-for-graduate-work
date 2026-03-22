package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Маппер для преобразования сущности пользователя в DTO и обратно.
 */
@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    /**
     * Преобразует DTO регистрации в сущность пользователя.
     *
     * @param register DTO регистрации
     * @return сущность пользователя
     */
    @Mapping(target = "email", source = "username")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity registerToEntity(Register register);

    /**
     * Преобразует сущность пользователя в DTO пользователя.
     *
     * @param userEntity сущность пользователя
     * @return DTO пользователя
     */
    @Mapping(target = "email", source = "email")
    @Mapping(target = "image", expression = "java(userEntity.getImage() != null && !userEntity.getImage().isBlank() ? \"/users/image/\" + userEntity.getId() : null)")
    User toDto(UserEntity userEntity);

    /**
     * Обновляет сущность пользователя данными из DTO.
     *
     * @param updateUser DTO с новыми данными пользователя
     * @param userEntity сущность пользователя
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateUserToEntity(UpdateUser updateUser, @MappingTarget UserEntity userEntity);
}