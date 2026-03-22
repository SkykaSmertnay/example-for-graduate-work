package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность объявления.
 * Используется для хранения данных объявления в базе данных.
 */
@Entity
@Table(name = "ads")
@Getter
@Setter
public class AdEntity {

    /**
     * Идентификатор объявления.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Заголовок объявления.
     */
    @Column(nullable = false, length = 32)
    private String title;

    /**
     * Описание объявления.
     */
    @Column(nullable = false, length = 64)
    private String description;

    /**
     * Цена объявления.
     */
    @Column(nullable = false)
    private Integer price;

    /**
     * Имя файла изображения объявления.
     */
    @Column
    private String image;

    /**
     * Автор объявления.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    /**
     * Список комментариев к объявлению.
     */
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();
}