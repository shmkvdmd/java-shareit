package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

public record BookingDto(
        LocalDateTime start,
        LocalDateTime end,
        Long itemId,
        Long bookerId,
        Booking.BookingStatus status) {
}
