package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.BookerDto;

@Component
public class BookingMapper {
    public Booking toEntity(BookingCreateDto dto) {
        return Booking.builder()
                .start(dto.start())
                .end(dto.end())
                .build();
    }

    public BookingDto toDto(Booking booking) {
        ItemShortDto itemDto = new ItemShortDto(booking.getItem().getId(), booking.getItem().getName());
        BookerDto bookerDto = new BookerDto(booking.getBooker().getId(), booking.getBooker().getName());
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDto,
                bookerDto,
                booking.getStatus()
        );
    }

    public BookingShortDto toShortDto(Booking booking) {
        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}