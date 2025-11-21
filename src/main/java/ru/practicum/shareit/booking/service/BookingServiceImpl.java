package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto create(BookingDto bookingDto) {
        return null;
    }

    @Override
    public BookingDto update(Long id, BookingDto bookingDto) {
        return null;
    }

    @Override
    public BookingDto getById(Long id) {
        return null;
    }

    @Override
    public List<BookingDto> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}
