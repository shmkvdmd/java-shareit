package ru.practicum.shareit.request.dto;

import lombok.Builder;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ItemRequestWithItemsDto(
        Long id,
        String description,
        Long requestorId,
        LocalDateTime created,
        List<ItemDto> items
) {
}
