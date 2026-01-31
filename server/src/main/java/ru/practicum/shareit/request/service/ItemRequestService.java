package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(Long userId, ItemRequestDto requestDto);

    List<ItemRequestWithItemsDto> getOwnRequests(Long userId);

    List<ItemRequestWithItemsDto> getAllRequests(Long userId);

    ItemRequestWithItemsDto getById(Long userId, Long requestId);
}