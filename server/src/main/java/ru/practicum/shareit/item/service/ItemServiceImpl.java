package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = getUserOrThrow(userId);
        ItemRequest request = itemDto.requestId() != null ? getRequestOrThrow(itemDto.requestId()) : null;

        Item item = itemMapper.toEntity(itemDto);
        item.setOwner(owner);
        item.setRequest(request);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = getItemOrThrow(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            log.warn("User {} tried to update item {} but not own (owner={})", userId, itemId, item.getOwner().getId());
            throw new NotFoundException(ExceptionConstants.ITEM_WRONG_OWNER.formatted(userId, itemId));
        }

        if (itemDto.name() != null && !itemDto.name().isBlank()) {
            item.setName(itemDto.name());
        }
        if (itemDto.description() != null && !itemDto.description().isBlank()) {
            item.setDescription(itemDto.description());
        }
        if (itemDto.available() != null) {
            item.setIsAvailable(itemDto.available());
        }
        if (itemDto.requestId() != null) {
            item.setRequest(getRequestOrThrow(itemDto.requestId()));
        }

        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDtoWithBooking getById(Long userId, Long itemId) {
        Item item = getItemOrThrow(itemId);
        List<CommentDto> comments = getCommentsForItem(itemId);

        BookingShortDto last = null;
        BookingShortDto next = null;

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            last = getLastApprovedBooking(itemId, now);
            next = getNextApprovedBooking(itemId, now);
        }

        return new ItemDtoWithBooking(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(), last, next, comments);
    }

    @Override
    public List<ItemDtoWithBooking> getAllByOwner(Long userId) {
        getUserOrThrow(userId);
        List<Item> items = itemRepository.findByOwnerIdOrderByIdAsc(userId);
        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    List<CommentDto> comments = getCommentsForItem(item.getId());
                    BookingShortDto last = getLastApprovedBooking(item.getId(), now);
                    BookingShortDto next = getNextApprovedBooking(item.getId(), now);
                    return new ItemDtoWithBooking(item.getId(), item.getName(), item.getDescription(),
                            item.getIsAvailable(), last, next, comments);
                })
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text)
                .stream()
                .filter(Item::getIsAvailable)
                .map(itemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CreateCommentDto commentDto) {
        User author = getUserOrThrow(userId);
        Item item = getItemOrThrow(itemId);

        if (!bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())) {
            log.warn("User {} tried to comment item {} without completed booking", userId, itemId);
            throw new ValidationException(ExceptionConstants.NO_COMPLETED_BOOKING_FOR_COMMENT);
        }

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setItem(item);
        comment.setCreator(author);
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    private List<CommentDto> getCommentsForItem(Long itemId) {
        return commentRepository.findByItemIdOrderByCreatedDesc(itemId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    private BookingShortDto getLastApprovedBooking(Long itemId, LocalDateTime now) {
        return bookingRepository.findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(
                        itemId, BookingStatus.APPROVED, now)
                .map(bookingMapper::toShortDto)
                .orElse(null);
    }

    private BookingShortDto getNextApprovedBooking(Long itemId, LocalDateTime now) {
        return bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                        itemId, BookingStatus.APPROVED, now)
                .map(bookingMapper::toShortDto)
                .orElse(null);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }

    private ItemRequest getRequestOrThrow(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.warn("ItemRequest with id={} not found", requestId);
                    return new NotFoundException(String.format(ExceptionConstants.REQUEST_NOT_FOUND, requestId));
                });
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id={} not found", itemId);
                    return new NotFoundException(String.format(ExceptionConstants.ITEM_NOT_FOUND, itemId));
                });
    }
}