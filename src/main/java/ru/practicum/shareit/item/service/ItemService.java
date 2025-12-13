package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDtoWithBooking getById(Long id, Long userId);

    List<ItemDtoWithBooking> getAllByOwner(Long userId);

    List<ItemDto> search(String text);

    void deleteById(Long id);

    CommentDto addComment(Long userId, Long itemId, CreateCommentDto commentDto);
}
