package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@Builder
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean isAvailable;
    private User owner;
    private ItemRequest request;
}
