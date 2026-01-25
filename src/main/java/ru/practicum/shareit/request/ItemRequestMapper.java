package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {

    private final ItemMapper itemMapper;

    public ItemRequestMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public ItemRequestDto toDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestorId(request.getRequestor().getId())
                .created(request.getCreated())
                .build();
    }

    public ItemRequestWithItemsDto toDtoWithItems(ItemRequest request) {
        List<ItemDto> items = request.getItems().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

        return ItemRequestWithItemsDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestorId(request.getRequestor().getId())
                .created(request.getCreated())
                .items(items)
                .build();
    }

    public ItemRequest toEntity(ItemRequestDto dto, User requestor) {
        return ItemRequest.builder()
                .id(dto.id())
                .description(dto.description())
                .requestor(requestor)
                .created(dto.created())
                .build();
    }
}