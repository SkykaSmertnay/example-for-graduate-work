package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "email", source = "username")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity registerToEntity(Register register);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "image", expression = "java(userEntity.getImage() != null && !userEntity.getImage().isBlank() ? \"/users/image/\" + userEntity.getId() : null)")
    User toDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateUserToEntity(UpdateUser updateUser, @MappingTarget UserEntity userEntity);
}