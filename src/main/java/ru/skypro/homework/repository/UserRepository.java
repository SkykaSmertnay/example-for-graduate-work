package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью пользователя.
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Ищет пользователя по email.
     *
     * @param email email пользователя
     * @return найденный пользователь или пустой результат
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email email пользователя
     * @return {@code true}, если пользователь существует, иначе {@code false}
     */
    boolean existsByEmail(String email);
}