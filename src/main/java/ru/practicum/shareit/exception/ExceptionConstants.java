package ru.practicum.shareit.exception;

public class ExceptionConstants {
    private ExceptionConstants() {
    }

    public static final String USER_NOT_FOUND = "User with id [%s] not found";
    public static final String ITEM_NOT_FOUND = "Item with id [%s] not found";
    public static final String REQUEST_NOT_FOUND = "Request with id [%s] not found";

    public static final String ITEM_WRONG_OWNER = "UserId=%s is not owner of itemId=%s, ownerId=%s";
    public static final String WRONG_EMAIL_FORMAT = "Wrong email format: %s";
    public static final String EMAIL_EXISTS = "Email exists: %s";
}
