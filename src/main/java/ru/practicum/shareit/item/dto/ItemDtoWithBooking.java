package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

public record ItemDtoWithBooking(
        Long id,
        String name,
        String description,
        Boolean available,
        BookingShortDto lastBooking,
        BookingShortDto nextBooking,
        List<CommentDto> comments) {
}