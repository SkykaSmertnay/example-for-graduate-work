# Ads Application Backend

## Описание проекта
Данный проект представляет собой backend-часть приложения для размещения и просмотра объявлений.

Пользователи могут:
- регистрироваться и авторизовываться;
- просматривать объявления;
- создавать, редактировать и удалять собственные объявления;
- загружать изображения для объявлений;
- оставлять комментарии к объявлениям;
- редактировать и удалять собственные комментарии;
- управлять своим профилем и аватаром.

Проект разработан на Java с использованием Spring Boot и PostgreSQL.  
Backend работает в связке с frontend-частью, предоставленной в рамках дипломного проекта.

## Основной функционал
В приложении реализованы следующие возможности:

- регистрация пользователя;
- авторизация пользователя;
- получение и обновление профиля;
- смена пароля;
- загрузка и получение аватара пользователя;
- просмотр всех объявлений без авторизации;
- просмотр одного объявления;
- создание объявления;
- редактирование и удаление собственного объявления;
- загрузка и получение изображения объявления;
- получение списка собственных объявлений пользователя;
- получение комментариев к объявлению;
- создание комментариев;
- редактирование и удаление собственных комментариев;
- разграничение прав доступа для ролей `USER` и `ADMIN`.

## Используемые технологии
В проекте используются следующие технологии и библиотеки:

- Java 17
- Spring Boot 2.7.15
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Liquibase
- MapStruct
- Lombok
- Swagger / OpenAPI (`springdoc-openapi-ui`)
- Maven

## Структура проекта
Проект разделён на основные слои:

- `controller` — REST-контроллеры для обработки HTTP-запросов;
- `service` — интерфейсы сервисного слоя;
- `service.impl` — реализации бизнес-логики;
- `repository` — доступ к данным через Spring Data JPA;
- `entity` — сущности базы данных;
- `dto` — модели передачи данных между backend и frontend;
- `mapper` — преобразование `Entity ↔ DTO` с использованием MapStruct;
- `config` — конфигурация приложения и Spring Security;
- `exception` — пользовательские исключения и глобальная обработка ошибок;
- `resources/db/changelog` — миграции Liquibase.

## Работа с базой данных
В качестве базы данных используется PostgreSQL.

Для управления схемой базы данных подключён Liquibase.  
Начальная структура базы создаётся через changelog-файлы при запуске приложения.

Основные таблицы:
- `users`
- `ads`
- `comments`

Также Liquibase создаёт служебные таблицы:
- `databasechangelog`
- `databasechangeloglock`

## Работа с изображениями
В проекте реализована загрузка и получение изображений:

- аватар пользователя;
- изображение объявления.

Изображения сохраняются в файловой системе, а в базе данных хранится имя файла.  
Frontend получает URL изображения в DTO, например:

- `/users/image/{id}`
- `/ads/image/{id}`

По этим адресам backend возвращает байты соответствующего изображения.

## Безопасность
В проекте используется Spring Security и HTTP Basic Authentication.

Реализовано разграничение доступа:
- неавторизованный пользователь может просматривать объявления;
- авторизованный пользователь может работать со своим профилем, объявлениями и комментариями;
- пользователь с ролью `ADMIN` имеет расширенные права доступа.

## API
Для тестирования API подключён Swagger UI.

После запуска приложения документация доступна по адресу:

`http://localhost:8080/swagger-ui/index.html`

## Участники проекта

- Сергей Гарилов — backend-разработка, настройка базы данных, реализация бизнес-логики, безопасность, работа с изображениями

## Запуск проекта

### 1. Клонировать репозиторий

```bash
git clone https://github.com/SkykaSmertnay/example-for-graduate-work
cd example-for-graduate-work
```

### 2. Создать базу данных PostgreSQL

Необходимо создать базу данных со следующими параметрами:

- database: `ads_db`
- username: `ads_user`
- password: `ads_password`

### 3. Настроить `application.properties`

Пример используемой конфигурации:

    spring.datasource.url=jdbc:postgresql://localhost:5432/ads_db
    spring.datasource.username=ads_user
    spring.datasource.password=ads_password

    spring.jpa.hibernate.ddl-auto=validate
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
    spring.sql.init.mode=never

    spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml

    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=10MB

    app.image.dir=images

### 4. Запустить приложение

Приложение можно запустить через IntelliJ IDEA или командой:

    mvn spring-boot:run

## Примеры реализованных endpoint'ов

### Аутентификация
- `POST /register`
- `POST /login`

### Пользователь
- `GET /users/me`
- `PATCH /users/me`
- `POST /users/set_password`
- `PATCH /users/me/image`
- `GET /users/image/{id}`

### Объявления
- `GET /ads`
- `GET /ads/{id}`
- `GET /ads/me`
- `POST /ads`
- `PATCH /ads/{id}`
- `DELETE /ads/{id}`
- `PATCH /ads/{id}/image`
- `GET /ads/image/{id}`

### Комментарии
- `GET /ads/{id}/comments`
- `POST /ads/{id}/comments`
- `PATCH /ads/{adId}/comments/{commentId}`
- `DELETE /ads/{adId}/comments/{commentId}`

## Особенности реализации

- пароли пользователей хранятся в зашифрованном виде с использованием `BCryptPasswordEncoder`;
- DTO соответствуют OpenAPI-спецификации;
- преобразование между сущностями и DTO выполнено через MapStruct;
- изображения пользователей и объявлений сохраняются в файловой системе;
- схема базы данных управляется через Liquibase;
- проект протестирован через Swagger UI.

## Возможные улучшения проекта

- покрытие кода тестами;
- добавление более подробных сообщений об ошибках;
- расширение валидации данных;
- добавление логирования;
- улучшение обработки изображений;
- реализация более гибкой системы авторизации.

## Статус проекта

Проект находится в стадии дипломной работы и демонстрирует работу backend-части сервиса объявлений с регистрацией, авторизацией, объявлениями, комментариями и изображениями.


