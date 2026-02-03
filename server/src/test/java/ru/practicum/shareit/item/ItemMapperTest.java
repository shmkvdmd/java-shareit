package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private final ItemMapper mapper = new ItemMapper();

    @Test
    void toDto() {
        User owner = User.builder().id(1L).build();
        ItemRequest request = ItemRequest.builder().id(10L).build();
        Item item = Item.builder()
                .id(1L)
                .name("test")
                .description("test desc")
                .isAvailable(true)
                .owner(owner)
                .request(request)
                .build();

        ItemDto dto = mapper.toDto(item);

        assertEquals(1L, dto.id());
        assertEquals("test", dto.name());
        assertEquals("test desc", dto.description());
        assertTrue(dto.available());
        assertEquals(10L, dto.requestId());
    }

    @Test
    void toDto_whenRequestNull() {
        User owner = User.builder().id(1L).build();
        Item item = Item.builder()
                .id(1L)
                .name("test")
                .description("test desc")
                .isAvailable(true)
                .owner(owner)
                .request(null)
                .build();

        ItemDto dto = mapper.toDto(item);

        assertNull(dto.requestId());
    }

    @Test
    void toEntity() {
        ItemDto dto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test desc")
                .available(true)
                .requestId(10L)
                .build();

        Item item = mapper.toEntity(dto);

        assertEquals(1L, item.getId());
        assertEquals("test", item.getName());
        assertEquals("test desc", item.getDescription());
        assertTrue(item.getIsAvailable());
        assertNull(item.getOwner());
        assertNull(item.getRequest());
    }
}