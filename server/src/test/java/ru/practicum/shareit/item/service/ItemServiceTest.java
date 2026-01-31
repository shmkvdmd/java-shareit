package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
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
    private ItemMapper itemMapper;

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
}