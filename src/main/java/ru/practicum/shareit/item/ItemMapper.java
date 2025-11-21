package ru.practicum.shareit.item;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        Long ownerId = item.getOwner() != null ? item.getOwner().getId() : null;
        Long requestId = item.getRequest() != null ? item.getRequest().getId() : null;
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                ownerId,
                requestId);
    }

    public static Item toItem(Long id, ItemDto itemDto, User user, ItemRequest itemRequest) {
        return Item.builder()
                .id(id)
                .name(itemDto.name())
                .description(itemDto.description())
                .isAvailable(itemDto.available())
                .owner(user)
                .request(itemRequest)
                .build();
    }
}
