package ru.practicum.shareit.exception;

public class ExceptionConstants {
    private ExceptionConstants() {
    }

    public static final String USER_NOT_FOUND = "User with id [%s] not found";
    public static final String ITEM_NOT_FOUND = "Item with id [%s] not found";
    public static final String REQUEST_NOT_FOUND = "Request with id [%s] not found";
    public static final String BOOKING_NOT_FOUND = "Booking with id [%s] not found";

    public static final String ITEM_WRONG_OWNER = "User %s is not the owner of item %s";
    public static final String BOOKING_ONLY_OWNER_OR_BOOKER = "Only owner or booker can view booking %s";
    public static final String BOOKING_STATUS_ALREADY_CHANGED = "Booking status already changed for id=%s";
    public static final String ITEM_NOT_AVAILABLE = "Item is not available for booking";
    public static final String NO_COMPLETED_BOOKING_FOR_COMMENT = "User has no completed approved booking for this item";
    public static final String ONLY_OWNER_CAN_APPROVE_BOOKING = "Only item owner can approve/reject booking";
}
