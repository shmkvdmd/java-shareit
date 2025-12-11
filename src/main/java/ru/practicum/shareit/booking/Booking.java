package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Booking {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Item item;
    private User creator;
    private BookingStatus status;

    public enum BookingStatus {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}

