package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import org.antlr.v4.runtime.misc.NotNull;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.user.dto.BookerDto;

import java.time.LocalDateTime;

@Builder
public record BookingDto(
        Long id,

        LocalDateTime start,

        LocalDateTime end,

        ItemShortDto item,

        BookerDto booker,

        BookingStatus status
) {
}