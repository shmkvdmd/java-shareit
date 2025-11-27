package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotImplementedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto create(BookingDto bookingDto) {
        throw new NotImplementedException(ExceptionConstants.METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public BookingDto update(Long id, BookingDto bookingDto) {
        throw new NotImplementedException(ExceptionConstants.METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public BookingDto getById(Long id) {
        throw new NotImplementedException(ExceptionConstants.METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public List<BookingDto> getAll() {
        throw new NotImplementedException(ExceptionConstants.METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException(ExceptionConstants.METHOD_NOT_IMPLEMENTED);
    }
}
