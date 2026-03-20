package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    CommentEntity createToEntity(CreateOrUpdateComment createOrUpdateComment);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "pk", source = "id")
    Comment toDto(CommentEntity commentEntity);

    List<Comment> toDtoList(List<CommentEntity> commentEntities);

    default Comments toCommentsDto(List<CommentEntity> commentEntities) {
        Comments comments = new Comments();
        comments.setCount(commentEntities.size());
        comments.setResults(toDtoList(commentEntities));
        return comments;
    }
}