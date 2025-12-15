package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.enums.BookingSearchState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingCreateDto createDto) {
        Item item = getItemOrThrow(createDto.itemId());
        User booker = getUserOrThrow(userId);

        if (item.getOwner().getId().equals(userId)) {
            log.warn("User {} attempted to book own item {}", userId, item.getId());
            throw new NotFoundException(ExceptionConstants.ITEM_WRONG_OWNER.formatted(userId, item.getId()));
        }
        if (!item.getIsAvailable()) {
            log.warn("User {} tried to book unavailable item {}", userId, item.getId());
            throw new ValidationException(ExceptionConstants.ITEM_NOT_AVAILABLE);
        }

        Booking booking = mapper.toEntity(createDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return mapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            log.warn("User {} tried to update booking {} but not owner", userId, bookingId);
            throw new ValidationException(ExceptionConstants.ONLY_OWNER_CAN_APPROVE_BOOKING);
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            log.warn("User {} tried to change status of processed booking {}", userId, bookingId);
            throw new ValidationException(ExceptionConstants.BOOKING_STATUS_ALREADY_CHANGED.formatted(bookingId));
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return mapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = getBookingOrThrow(bookingId);
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        if (!userId.equals(ownerId) && !userId.equals(bookerId)) {
            log.warn("User {} tried to view booking {} without permission (owner={}, booker={})",
                    userId, bookingId, ownerId, bookerId);
            throw new NotFoundException(ExceptionConstants.BOOKING_ONLY_OWNER_OR_BOOKER.formatted(bookingId));
        }

        return mapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByUser(Long userId, BookingSearchState state) {
        getUserOrThrow(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerId(userId, sort);
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, now, now, sort);
            case PAST -> bookingRepository.findByBookerIdAndEndBefore(userId, now, sort);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfter(userId, now, sort);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, sort);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, sort);
        };

        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long userId, BookingSearchState state) {
        getUserOrThrow(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItemOwnerId(userId, sort);
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(userId, now, now, sort);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBefore(userId, now, sort);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfter(userId, now, sort);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, sort);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, sort);
        };

        return bookings.stream().map(mapper::toDto).toList();
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item with id={} not found", itemId);
                    return new NotFoundException(String.format(ExceptionConstants.ITEM_NOT_FOUND, itemId));
                });
    }

    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id={} not found", bookingId);
                    return new NotFoundException(String.format(ExceptionConstants.BOOKING_NOT_FOUND, bookingId));
                });
    }
}