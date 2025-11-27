package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        User user = findUserByIdOrThrow(userId);

        ItemRequest itemRequest = null;
        if (itemDto.requestId() != null) {
            itemRequest = findItemRequestByIdOrThrow(itemDto.requestId());
        }

        Item item = itemMapper.toEntity(getNextId(), itemDto, user, itemRequest);
        Item saved = itemRepository.createItem(item);
        return itemMapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long id, ItemDto itemDto, Long userId) {
        Item item = findItemByIdOrThrow(id);

        if (item.getOwner() == null || !item.getOwner().getId().equals(userId)) {
            log.warn("update: userId={} is not owner of itemId={}, ownerId={}",
                    userId, id, item.getOwner() == null ? null : item.getOwner().getId());
            throw new NotFoundException(String.format(ExceptionConstants.ITEM_WRONG_OWNER,
                    userId, id, item.getOwner() == null ? null : item.getOwner().getId()));
        }

        if (itemDto.name() != null) {
            item.setName(itemDto.name());
        }
        if (itemDto.description() != null) {
            item.setDescription(itemDto.description());
        }
        if (itemDto.available() != null) {
            item.setIsAvailable(itemDto.available());
        }
        if (itemDto.requestId() != null) {
            item.setRequest(findItemRequestByIdOrThrow(itemDto.requestId()));
        }
        Item updated = itemRepository.updateItem(item);
        return itemMapper.toDto(updated);
    }

    @Override
    public ItemDto getById(Long id) {
        Item item = findItemByIdOrThrow(id);
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getByText(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<Item> items = itemRepository.findItemsByText(text);
        return items.stream().map(itemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> getAllByUserId(Long userId) {
        List<Item> items = itemRepository.findAllItems();
        return items.stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(itemMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteItemById(id);
    }

    private long getNextId() {
        return itemRepository.findAllItems().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0) + 1;
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }

    private Item findItemByIdOrThrow(Long itemId) {
        return itemRepository.findItemById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id={} not found", itemId);
                    return new NotFoundException(String.format(ExceptionConstants.ITEM_NOT_FOUND, itemId));
                });
    }

    private ItemRequest findItemRequestByIdOrThrow(Long requestId) {
        return itemRequestRepository.findItemRequestById(requestId)
                .orElseThrow(() -> {
                    log.warn("Item request with id={} not found", requestId);
                    return new NotFoundException(String.format(ExceptionConstants.REQUEST_NOT_FOUND, requestId));
                });
    }

}
