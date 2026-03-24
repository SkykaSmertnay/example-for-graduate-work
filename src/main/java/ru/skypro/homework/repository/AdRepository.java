package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностью объявления.
 */
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Возвращает список объявлений по идентификатору автора.
     *
     * @param authorId идентификатор автора
     * @return список объявлений пользователя
     */
    List<AdEntity> findAllByAuthorId(Integer authorId);
}