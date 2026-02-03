package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private final BookingMapper mapper = new BookingMapper();

    @Test
    void toEntity() {
        BookingCreateDto dto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );

        Booking entity = mapper.toEntity(dto);

        assertEquals(dto.start(), entity.getStart());
        assertEquals(dto.end(), entity.getEnd());
        assertNull(entity.getId());
        assertNull(entity.getItem());
        assertNull(entity.getBooker());
        assertNull(entity.getStatus());
    }

    @Test
    void toDto() {
        User booker = User.builder().id(2L).name("user").build();
        Item item = Item.builder().id(1L).name("item").build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        BookingDto dto = mapper.toDto(booking);

        assertEquals(1L, dto.id());
        assertEquals(booking.getStart(), dto.start());
        assertEquals(booking.getEnd(), dto.end());
        assertEquals(1L, dto.item().id());
        assertEquals("item", dto.item().name());
        assertEquals(2L, dto.booker().id());
        assertEquals("user", dto.booker().name());
        assertEquals(BookingStatus.WAITING, dto.status());
    }

    @Test
    void toShortDto() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingShortDto shortDto = mapper.toShortDto(booking);

        assertEquals(1L, shortDto.id());
        assertEquals(2L, shortDto.bookerId());
        assertEquals(booking.getStart(), shortDto.start());
        assertEquals(booking.getEnd(), shortDto.end());
    }
}