package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User creator;
    private LocalDateTime created;
}
