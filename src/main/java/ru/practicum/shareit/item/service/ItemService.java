package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(Long id, ItemDto itemDto, Long userId);

    List<ItemDto> getByText(String text);

    ItemDto getById(Long id);

    List<ItemDto> getAllByUserId(Long userId);

    void delete(Long id);
}
