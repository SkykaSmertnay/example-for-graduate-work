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
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentsService;

import java.util.List;
import ru.skypro.homework.exception.NotFoundException;

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

    @Override
    public Comments getComments(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new NotFoundException(AD_NOT_FOUND_MESSAGE);
        }

        List<CommentEntity> commentEntities = commentRepository.findAllByAdId(adId);
        return commentMapper.toCommentsDto(commentEntities);
    }

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

    @Override
    public void deleteComment(Integer adId, Integer commentId, String email) {
        UserEntity currentUser = getUserByEmail(email);

        CommentEntity commentEntity = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND_MESSAGE));

        checkCommentAccess(commentEntity, currentUser);

        commentRepository.delete(commentEntity);
    }

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

    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    private void checkCommentAccess(CommentEntity commentEntity, UserEntity currentUser) {
        boolean isAuthor = commentEntity.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException(FORBIDDEN_MESSAGE);
        }
    }
}