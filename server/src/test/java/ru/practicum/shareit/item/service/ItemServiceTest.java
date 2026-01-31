package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void create_whenValid_shouldReturnItemDto() {
        Long userId = 1L;
        ItemDto dto = ItemDto.builder()
                .name("test")
                .description("desc")
                .available(true)
                .build();

        User owner = User.builder().id(userId).build();
        Item savedItem = Item.builder().id(1L).owner(owner).build();

        ItemDto expectedDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("desc")
                .available(true)
                .requestId(null)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemMapper.toEntity(dto)).thenReturn(savedItem);
        when(itemRepository.save(savedItem)).thenReturn(savedItem);
        when(itemMapper.toDto(savedItem)).thenReturn(expectedDto);

        ItemDto result = itemService.create(userId, dto);

        assertEquals(expectedDto, result);
        verify(itemRepository).save(savedItem);
    }

    @Test
    void create_whenUserNotFound_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.create(1L, ItemDto.builder().build()));
    }

    @Test
    void update_whenNotOwner_shouldThrowNotFound() {
        Item item = Item.builder().owner(User.builder().id(2L).build()).build();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, ItemDto.builder().build()));
    }

    @Test
    void update_whenValid_shouldUpdateFieldsAndReturnDto() {
        User owner = User.builder().id(1L).build();
        Item item = Item.builder()
                .id(1L)
                .owner(owner)
                .name("Old")
                .description("Old desc")
                .isAvailable(true)
                .build();

        ItemDto updateDto = ItemDto.builder()
                .name("New")
                .description("New desc")
                .available(false)
                .build();

        ItemDto expectedDto = ItemDto.builder()
                .id(1L)
                .name("New")
                .description("New desc")
                .available(false)
                .requestId(null)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(expectedDto);

        ItemDto result = itemService.update(1L, 1L, updateDto);

        assertEquals("New", item.getName());
        assertEquals("New desc", item.getDescription());
        assertFalse(item.getIsAvailable());
        assertEquals(expectedDto, result);
    }

    @Test
    void addComment_whenNoCompletedBooking_shouldThrowValidation() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(Item.builder().build()));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                anyLong(), anyLong(), any(), any(LocalDateTime.class))).thenReturn(false);

        assertThrows(ValidationException.class, () -> itemService.addComment(1L, 1L, new CreateCommentDto("Text")));
    }

    @Test
    void getById_whenItemNotFound_shouldThrowNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getById(1L, 1L));
    }

    @Test
    void search_whenTextBlank_shouldReturnEmptyList() {
        List<ItemDto> result = itemService.search(" ");

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        itemService.deleteById(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    void search_whenValidText_shouldReturnItems() {
        String text = "text";
        Item item = Item.builder().id(1L).isAvailable(true).build();
        ItemDto dto = ItemDto.builder().id(1L).build();

        when(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text))
                .thenReturn(List.of(item));
        when(itemMapper.toDto(item)).thenReturn(dto);

        List<ItemDto> result = itemService.search(text);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void update_shouldUpdateOnlyAvailableStatus() {
        User owner = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(owner).isAvailable(true).name("Old").build();
        ItemDto updateDto = ItemDto.builder().available(false).build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        itemService.update(1L, 1L, updateDto);

        assertFalse(item.getIsAvailable());
        assertEquals("Old", item.getName());
    }

    @Test
    void getById_whenOwner_shouldIncludeBookings() {
        Long userId = 1L;
        Long itemId = 1L;
        User owner = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).owner(owner).build();

        Booking booking = new ru.practicum.shareit.booking.Booking();
        BookingShortDto bookingShortDto = new BookingShortDto(1L, 1L, LocalDateTime.now(), LocalDateTime.now());

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemIdOrderByCreatedDesc(itemId)).thenReturn(List.of());
        when(bookingRepository.findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(anyLong(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(Optional.of(booking));

        when(bookingMapper.toShortDto(any())).thenReturn(bookingShortDto);

        var result = itemService.getById(userId, itemId);

        assertNotNull(result);
        assertNotNull(result.lastBooking());
        assertNotNull(result.nextBooking());
        verify(bookingRepository, times(1)).findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(any(), any(), any());
    }

    @Test
    void getAllByOwner_shouldReturnItemsWithBookings() {
        Long userId = 1L;
        User owner = User.builder().id(userId).build();
        Item item = Item.builder().id(1L).owner(owner).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerIdOrderByIdAsc(userId)).thenReturn(List.of(item));
        when(commentRepository.findByItemIdOrderByCreatedDesc(anyLong())).thenReturn(List.of());

        var result = itemService.getAllByOwner(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void create_withRequest_shouldSetRequest() {
        Long userId = 1L;
        Long requestId = 10L;
        ItemDto dto = ItemDto.builder().name("Item").requestId(requestId).build();
        User owner = User.builder().id(userId).build();
        ItemRequest request = ItemRequest.builder().id(requestId).build();
        Item item = Item.builder().name("Item").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemMapper.toEntity(dto)).thenReturn(item);
        when(itemRepository.save(any())).thenReturn(item);
        when(itemMapper.toDto(any())).thenReturn(dto);

        ItemDto result = itemService.create(userId, dto);

        assertNotNull(result);
        verify(itemRequestRepository).findById(requestId);
    }
}