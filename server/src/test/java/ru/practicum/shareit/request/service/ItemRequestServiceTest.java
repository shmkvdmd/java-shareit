package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestMapper mapper;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @Test
    void create_whenValid_shouldReturnDto() {
        Long userId = 1L;
        ItemRequestDto dto = ItemRequestDto.builder()
                .description("desc")
                .build();

        User requestor = User.builder().id(userId).build();
        ItemRequest savedRequest = ItemRequest.builder().id(1L).requestor(requestor).created(LocalDateTime.now()).build();

        ItemRequestDto expectedDto = ItemRequestDto.builder()
                .id(1L)
                .description("desc")
                .requestorId(userId)
                .created(savedRequest.getCreated())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(mapper.toEntity(dto, requestor)).thenReturn(savedRequest);
        when(requestRepository.save(savedRequest)).thenReturn(savedRequest);
        when(mapper.toDto(savedRequest)).thenReturn(expectedDto);

        ItemRequestDto result = requestService.create(userId, dto);

        assertEquals(expectedDto, result);
        verify(requestRepository).save(savedRequest);
    }

    @Test
    void create_whenUserNotFound_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.create(1L, ItemRequestDto.builder().build()));
    }

    @Test
    void getOwnRequests_whenUserExists_shouldReturnList() {
        Long userId = 1L;
        User requestor = User.builder().id(userId).build();
        ItemRequest request = ItemRequest.builder().id(1L).requestor(requestor).build();
        ItemRequestWithItemsDto dto = ItemRequestWithItemsDto.builder().id(1L).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(requestRepository.findByRequestorIdOrderByCreatedDesc(userId)).thenReturn(List.of(request));
        when(mapper.toDtoWithItems(request)).thenReturn(dto);

        List<ItemRequestWithItemsDto> result = requestService.getOwnRequests(userId);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test
    void getOwnRequests_whenUserNotFound_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getOwnRequests(1L));
    }

    @Test
    void getAllRequests_whenUserExists_shouldReturnOthersList() {
        Long userId = 1L;
        User requestor = User.builder().id(userId).build();
        ItemRequest otherRequest = ItemRequest.builder().id(2L).build();
        ItemRequestWithItemsDto dto = ItemRequestWithItemsDto.builder().id(2L).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(requestRepository.findByRequestorIdNotOrderByCreatedDesc(userId)).thenReturn(List.of(otherRequest));
        when(mapper.toDtoWithItems(otherRequest)).thenReturn(dto);

        List<ItemRequestWithItemsDto> result = requestService.getAllRequests(userId);

        assertEquals(1, result.size());
        assertEquals(2L, result.getFirst().id());
    }

    @Test
    void getById_whenRequestNotFound_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getById(1L, 1L));
    }

    @Test
    void getById_whenValid_shouldReturnDtoWithItems() {
        Long userId = 1L;
        Long requestId = 1L;
        User requestor = User.builder().id(userId).build();
        ItemRequest request = ItemRequest.builder().id(requestId).requestor(requestor).build();
        ItemRequestWithItemsDto expected = ItemRequestWithItemsDto.builder().id(requestId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(mapper.toDtoWithItems(request)).thenReturn(expected);

        ItemRequestWithItemsDto result = requestService.getById(userId, requestId);

        assertEquals(expected, result);
    }
}