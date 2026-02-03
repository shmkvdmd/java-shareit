package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ItemRequestMapperTest {

    private final ItemMapper itemMapper = mock(ItemMapper.class);
    private final ItemRequestMapper mapper = new ItemRequestMapper(itemMapper);

    @Test
    void toDto() {
        User requestor = User.builder().id(1L).build();
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("desc")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto dto = mapper.toDto(request);

        assertEquals(1L, dto.id());
        assertEquals("desc", dto.description());
        assertEquals(1L, dto.requestorId());
        assertNotNull(dto.created());
    }

    @Test
    void toDtoWithItems() {
        User requestor = User.builder().id(1L).build();
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("desc")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        ItemRequestWithItemsDto dto = mapper.toDtoWithItems(request);

        assertEquals(1L, dto.id());
        assertEquals("desc", dto.description());
        assertEquals(1L, dto.requestorId());
        assertNotNull(dto.created());
        assertTrue(dto.items().isEmpty());
    }

    @Test
    void toEntity() {
        User requestor = User.builder().id(1L).build();
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(null)
                .description("desc")
                .build();

        ItemRequest entity = mapper.toEntity(dto, requestor);

        assertNull(entity.getId());
        assertEquals("desc", entity.getDescription());
        assertEquals(requestor, entity.getRequestor());
        assertNotNull(entity.getCreated());
    }
}