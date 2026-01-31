package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.user.dto.BookerDto;

import java.time.LocalDateTime;

@Builder
public record BookingDto(
        Long id,

        @NotNull
        @FutureOrPresent
        LocalDateTime start,

        @NotNull
        @Future
        LocalDateTime end,

        ItemShortDto item,

        BookerDto booker,

        BookingStatus status
) {
}