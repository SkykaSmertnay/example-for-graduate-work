package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое в случае,
 * если запрашиваемый объект не найден.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message текст сообщения об ошибке
     */
    public NotFoundException(String message) {
        super(message);
    }
}