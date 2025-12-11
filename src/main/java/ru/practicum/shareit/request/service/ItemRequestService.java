package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto);

    ItemRequestDto update(Long id, ItemRequestDto itemRequestDto);

    ItemRequestDto getById(Long id);

    List<ItemRequestDto> getAll();

    void delete(Long id);
}
