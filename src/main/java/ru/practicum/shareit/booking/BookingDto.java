package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingDto(
        LocalDateTime start,
        LocalDateTime end,
        Long itemId,
        Long bookerId,
        BookingStatus status) {
}
