package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {
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

    public Item toEntity(Long id, ItemDto itemDto, User user, ItemRequest itemRequest) {
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
