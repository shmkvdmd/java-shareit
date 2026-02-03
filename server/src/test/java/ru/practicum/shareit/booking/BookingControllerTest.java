package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingSearchState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        BookingCreateDto requestDto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );

        BookingDto responseDto = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.create(anyLong(), any(BookingCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void updateStatus_shouldReturnUpdatedBooking() throws Exception {
        BookingDto responseDto = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingService.updateStatus(anyLong(), anyLong(), eq(true))).thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBooking_shouldReturnBooking() throws Exception {
        BookingDto responseDto = BookingDto.builder().id(1L).build();

        when(bookingService.getById(anyLong(), anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getBookings_shouldReturnUserBookings() throws Exception {
        BookingDto booking = BookingDto.builder().id(1L).build();

        when(bookingService.getBookingsByUser(anyLong(), eq(BookingSearchState.ALL))).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getBookingsByOwner_shouldReturnOwnerBookings() throws Exception {
        BookingDto booking = BookingDto.builder().id(1L).build();

        when(bookingService.getBookingsByOwner(anyLong(), eq(BookingSearchState.ALL))).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getBookings_whenNoBookings_shouldReturnEmptyList() throws Exception {
        when(bookingService.getBookingsByUser(anyLong(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}