package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Маппер для преобразования сущности комментария в DTO и обратно.
 */
@Mapper(config = MapStructConfig.class)
public interface CommentMapper {

    /**
     * Преобразует DTO создания/обновления комментария в сущность комментария.
     *
     * @param createOrUpdateComment DTO с данными комментария
     * @return сущность комментария
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity createToEntity(CreateOrUpdateComment createOrUpdateComment);

    /**
     * Преобразует сущность комментария в DTO комментария.
     *
     * @param commentEntity сущность комментария
     * @return DTO комментария
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorImage", expression = "java(commentEntity.getAuthor().getImage() != null && !commentEntity.getAuthor().getImage().isBlank() ? \"/users/image/\" + commentEntity.getAuthor().getId() : null)")
    Comment toDto(CommentEntity commentEntity);

    /**
     * Преобразует список сущностей комментариев в список DTO.
     *
     * @param commentEntities список сущностей комментариев
     * @return список DTO комментариев
     */
    List<Comment> toDtoList(List<CommentEntity> commentEntities);

    /**
     * Формирует DTO со списком комментариев и их количеством.
     *
     * @param commentEntities список сущностей комментариев
     * @return DTO со списком комментариев
     */
    default Comments toCommentsDto(List<CommentEntity> commentEntities) {
        Comments comments = new Comments();
        comments.setCount(commentEntities.size());
        comments.setResults(toDtoList(commentEntities));
        return comments;
    }
}