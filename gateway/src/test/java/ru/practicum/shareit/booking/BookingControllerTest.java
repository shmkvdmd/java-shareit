package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void shouldCreateBooking() throws Exception {
        BookingCreateDto dto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );
        when(bookingClient.create(anyLong(), any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailCreateBookingInvalidDates() throws Exception {
        BookingCreateDto dto = new BookingCreateDto(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1),
                1L
        );

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateStatus() throws Exception {
        when(bookingClient.updateStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(bookingClient.getById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserBookings() throws Exception {
        when(bookingClient.getBookingsByUser(anyLong(), any(), any(), any()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOwnerBookings() throws Exception {
        when(bookingClient.getBookingsByOwner(anyLong(), any(), any(), any()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }
}