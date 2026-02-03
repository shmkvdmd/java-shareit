package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

public record BookingCreateDto(
        LocalDateTime start,

        LocalDateTime end,

        Long itemId
) {
}