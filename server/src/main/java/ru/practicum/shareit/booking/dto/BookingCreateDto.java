package ru.practicum.shareit.booking.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

public record BookingCreateDto(
        LocalDateTime start,

        LocalDateTime end,

        Long itemId
) {
}