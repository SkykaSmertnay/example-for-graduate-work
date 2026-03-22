package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentsService;

import java.util.List;

/**
 * Реализация сервиса для работы с комментариями к объявлениям.
 * Содержит бизнес-логику получения, создания, изменения и удаления комментариев.
 */
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private static final String AD_NOT_FOUND_MESSAGE = "Ad not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment not found";
    private static final String FORBIDDEN_MESSAGE = "Access denied";

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Возвращает список комментариев для указанного объявления.
     *
     * @param adId идентификатор объявления
     * @return объект со списком комментариев и их количеством
     */
    @Override
    public Comments getComments(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new NotFoundException(AD_NOT_FOUND_MESSAGE);
        }

        List<CommentEntity> commentEntities = commentRepository.findAllByAdId(adId);
        return commentMapper.toCommentsDto(commentEntities);
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param adId идентификатор объявления
     * @param email email пользователя
     * @param createOrUpdateComment данные нового комментария
     * @return созданный комментарий
     */
    @Override
    public Comment addComment(Integer adId, String email, CreateOrUpdateComment createOrUpdateComment) {
        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException(AD_NOT_FOUND_MESSAGE));

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        CommentEntity commentEntity = commentMapper.createToEntity(createOrUpdateComment);
        commentEntity.setAd(adEntity);
        commentEntity.setAuthor(userEntity);
        commentEntity.setCreatedAt(System.currentTimeMillis());

        CommentEntity savedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(savedComment);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     * Удаление доступно только автору комментария или администратору.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param email email пользователя
     */
    @Override
    public void deleteComment(Integer adId, Integer commentId, String email) {
        UserEntity currentUser = getUserByEmail(email);

        CommentEntity commentEntity = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND_MESSAGE));

        checkCommentAccess(commentEntity, currentUser);

        commentRepository.delete(commentEntity);
    }

    /**
     * Обновляет комментарий по его идентификатору.
     * Обновление доступно только автору комментария или администратору.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param email email пользователя
     * @param createOrUpdateComment данные для обновления комментария
     * @return обновлённый комментарий
     */
    @Override
    public Comment updateComment(Integer adId, Integer commentId, String email, CreateOrUpdateComment createOrUpdateComment) {
        UserEntity currentUser = getUserByEmail(email);

        CommentEntity commentEntity = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND_MESSAGE));

        checkCommentAccess(commentEntity, currentUser);

        commentEntity.setText(createOrUpdateComment.getText());
        CommentEntity savedComment = commentRepository.save(commentEntity);

        return commentMapper.toDto(savedComment);
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
     * Проверяет, имеет ли пользователь право изменять комментарий.
     * Доступ разрешён автору комментария или пользователю с ролью ADMIN.
     *
     * @param commentEntity комментарий
     * @param currentUser текущий пользователь
     */
    private void checkCommentAccess(CommentEntity commentEntity, UserEntity currentUser) {
        boolean isAuthor = commentEntity.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException(FORBIDDEN_MESSAGE);
        }
    }
}