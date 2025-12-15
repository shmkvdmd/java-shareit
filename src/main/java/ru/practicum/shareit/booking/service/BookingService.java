package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingSearchState;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingCreateDto bookingDto);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByOwner(Long userId, BookingSearchState state);

    List<BookingDto> getBookingsByUser(Long userId, BookingSearchState state);
}
