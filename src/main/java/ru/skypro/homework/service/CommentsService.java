package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

/**
 * Сервис для работы с комментариями к объявлениям.
 * Содержит методы для получения, создания, изменения и удаления комментариев.
 */
public interface CommentsService {

    /**
     * Возвращает список комментариев для указанного объявления.
     *
     * @param adId идентификатор объявления
     * @return объект со списком комментариев и их количеством
     */
    Comments getComments(Integer adId);

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param adId идентификатор объявления
     * @param email email пользователя
     * @param createOrUpdateComment данные нового комментария
     * @return созданный комментарий
     */
    Comment addComment(Integer adId, String email, CreateOrUpdateComment createOrUpdateComment);

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param email email пользователя
     */
    void deleteComment(Integer adId, Integer commentId, String email);

    /**
     * Обновляет комментарий по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param email email пользователя
     * @param createOrUpdateComment данные для обновления комментария
     * @return обновлённый комментарий
     */
    Comment updateComment(Integer adId, Integer commentId, String email, CreateOrUpdateComment createOrUpdateComment);
}