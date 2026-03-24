package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью комментария.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Возвращает список комментариев для указанного объявления.
     *
     * @param adId идентификатор объявления
     * @return список комментариев
     */
    List<CommentEntity> findAllByAdId(Integer adId);

    /**
     * Возвращает комментарий по его идентификатору и идентификатору объявления.
     *
     * @param id идентификатор комментария
     * @param adId идентификатор объявления
     * @return найденный комментарий или пустой результат
     */
    Optional<CommentEntity> findByIdAndAdId(Integer id, Integer adId);
}