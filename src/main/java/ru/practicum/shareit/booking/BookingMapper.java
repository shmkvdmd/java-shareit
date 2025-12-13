package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.ItemShortDto;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        ItemShortDto itemDto = new ItemShortDto(booking.getItem().getId(), booking.getItem().getName());
        BookerDto bookerDto = new BookerDto(booking.getBooker().getId());
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