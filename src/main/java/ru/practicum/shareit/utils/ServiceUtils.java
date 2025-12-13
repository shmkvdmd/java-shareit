package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceUtils {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }

    public Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id={} not found", itemId);
                    return new NotFoundException(String.format(ExceptionConstants.ITEM_NOT_FOUND, itemId));
                });
    }

    public ItemRequest getRequestOrThrow(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.warn("ItemRequest with id={} not found", requestId);
                    return new NotFoundException(String.format(ExceptionConstants.REQUEST_NOT_FOUND, requestId));
                });
    }

    public Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id={} not found", bookingId);
                    return new NotFoundException(String.format(ExceptionConstants.BOOKING_NOT_FOUND, bookingId));
                });
    }
}