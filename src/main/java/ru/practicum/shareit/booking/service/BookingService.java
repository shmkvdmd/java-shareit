package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDto bookingDto);

    BookingDto update(Long id, BookingDto bookingDto);

    BookingDto getById(Long id);

    List<BookingDto> getAll();

    void delete(Long id);
}
