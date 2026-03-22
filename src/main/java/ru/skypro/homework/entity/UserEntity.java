package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

/**
 * Сущность пользователя.
 * Используется для хранения данных пользователя в базе данных.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Email пользователя.
     */
    @Column(nullable = false, unique = true, length = 32)
    private String email;

    /**
     * Зашифрованный пароль пользователя.
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * Имя пользователя.
     */
    @Column(nullable = false, length = 16)
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Column(nullable = false, length = 16)
    private String lastName;

    /**
     * Телефон пользователя.
     */
    @Column(nullable = false, length = 32)
    private String phone;

    /**
     * Роль пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Имя файла изображения пользователя.
     */
    @Column
    private String image;
}