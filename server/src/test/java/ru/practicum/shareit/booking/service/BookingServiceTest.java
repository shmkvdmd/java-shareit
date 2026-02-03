package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingSearchState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper mapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void create_whenValid_shouldReturnBookingDto() {
        Long userId = 2L;
        BookingCreateDto createDto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L
        );

        User booker = User.builder().id(userId).build();
        User owner = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(owner).isAvailable(true).build();
        Booking savedBooking = Booking.builder().id(1L).status(BookingStatus.WAITING).build();
        BookingDto expectedDto = BookingDto.builder().id(1L).status(BookingStatus.WAITING).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(mapper.toEntity(createDto)).thenReturn(savedBooking);
        when(bookingRepository.save(savedBooking)).thenReturn(savedBooking);
        when(mapper.toDto(savedBooking)).thenReturn(expectedDto);

        BookingDto result = bookingService.create(userId, createDto);

        assertEquals(expectedDto, result);
        assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
        verify(bookingRepository).save(savedBooking);
    }

    @Test
    void create_whenOwnItem_shouldThrowNotFound() {
        Long userId = 1L;
        BookingCreateDto createDto = new BookingCreateDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L);

        User owner = User.builder().id(userId).build();
        Item item = Item.builder().owner(owner).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.create(userId, createDto));
    }

    @Test
    void create_whenItemUnavailable_shouldThrowValidation() {
        Long userId = 2L;
        BookingCreateDto createDto = new BookingCreateDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L);

        User booker = User.builder().id(userId).build();
        User owner = User.builder().id(10L).build();
        Item item = Item.builder()
                .owner(owner)
                .isAvailable(false)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.create(userId, createDto));
    }

    @Test
    void updateStatus_whenApproved_shouldSetApproved() {
        Long ownerId = 1L;
        Long bookingId = 1L;

        User owner = User.builder().id(ownerId).build();
        Item item = Item.builder().owner(owner).build();
        Booking booking = Booking.builder().id(bookingId).item(item).status(BookingStatus.WAITING).build();
        BookingDto expectedDto = BookingDto.builder().id(bookingId).status(BookingStatus.APPROVED).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(mapper.toDto(booking)).thenReturn(expectedDto);

        BookingDto result = bookingService.updateStatus(ownerId, bookingId, true);

        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        assertEquals(expectedDto, result);
    }

    @Test
    void updateStatus_whenNotOwner_shouldThrowValidation() {
        Long notOwnerId = 3L;
        Long bookingId = 1L;

        User owner = User.builder().id(1L).build();
        Item item = Item.builder().owner(owner).build();
        Booking booking = Booking.builder().item(item).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.updateStatus(notOwnerId, bookingId, true));
    }

    @Test
    void getById_whenValid_shouldReturnDto() {
        Long userId = 2L;
        Long bookingId = 1L;

        User booker = User.builder().id(userId).build();
        User owner = User.builder().id(10L).build();

        Item item = Item.builder()
                .id(5L)
                .owner(owner)
                .build();

        Booking booking = Booking.builder()
                .id(bookingId)
                .booker(booker)
                .item(item)
                .build();

        BookingDto expectedDto = BookingDto.builder().id(bookingId).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toDto(booking)).thenReturn(expectedDto);

        BookingDto result = bookingService.getById(userId, bookingId);

        assertEquals(expectedDto, result);
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getById_whenNotOwnerOrBooker_shouldThrowNotFound() {
        Long strangerId = 3L;
        Long bookingId = 1L;

        User booker = User.builder().id(2L).build();
        User owner = User.builder().id(1L).build();
        Item item = Item.builder().owner(owner).build();
        Booking booking = Booking.builder().booker(booker).item(item).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getById(strangerId, bookingId));
    }

    @ParameterizedTest
    @EnumSource(BookingSearchState.class)
    void getBookingsByUser_shouldWorkForAllStates(BookingSearchState state) {
        Long userId = 1L;
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        lenient().when(bookingRepository.findByBookerId(anyLong(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByBookerIdAndEndBefore(anyLong(), any(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByBookerIdAndStartAfter(anyLong(), any(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByBookerIdAndStatus(anyLong(), any(), any())).thenReturn(List.of());

        List<BookingDto> result = bookingService.getBookingsByUser(userId, state);

        assertNotNull(result);
        verify(userRepository).findById(userId);
    }

    @ParameterizedTest
    @EnumSource(BookingSearchState.class)
    void getBookingsByOwner_shouldWorkForAllStates(BookingSearchState state) {
        Long userId = 1L;
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        lenient().when(bookingRepository.findByItemOwnerId(anyLong(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByItemOwnerIdAndEndBefore(anyLong(), any(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByItemOwnerIdAndStartAfter(anyLong(), any(), any())).thenReturn(List.of());
        lenient().when(bookingRepository.findByItemOwnerIdAndStatus(anyLong(), any(), any())).thenReturn(List.of());

        List<BookingDto> result = bookingService.getBookingsByOwner(userId, state);

        assertNotNull(result);
    }

    @Test
    void updateStatus_whenStatusAlreadyChanged_shouldThrowValidation() {
        Long ownerId = 1L;
        Long bookingId = 1L;
        User owner = User.builder().id(ownerId).build();
        Item item = Item.builder().owner(owner).build();
        Booking booking = Booking.builder().item(item).status(BookingStatus.APPROVED).build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.updateStatus(ownerId, bookingId, true));
    }
}