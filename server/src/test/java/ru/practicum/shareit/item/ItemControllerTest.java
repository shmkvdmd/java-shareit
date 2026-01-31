package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void create_shouldReturnCreatedItem() throws Exception {
        ItemDto requestDto = ItemDto.builder()
                .name("test")
                .description("test desc")
                .available(true)
                .build();

        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test desc")
                .available(true)
                .build();

        when(itemService.create(anyLong(), any(ItemDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void update_shouldReturnUpdatedItem() throws Exception {
        ItemDto requestDto = ItemDto.builder()
                .name("Updated test")
                .build();

        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Updated test")
                .build();

        when(itemService.update(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated test"));
    }

    @Test
    void getById_shouldReturnItemWithBooking() throws Exception {
        ItemDtoWithBooking response = new ItemDtoWithBooking(1L, "test", "Desc", true, null, null, Collections.emptyList());

        when(itemService.getById(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllByOwner_shouldReturnOwnerItems() throws Exception {
        ItemDtoWithBooking item = new ItemDtoWithBooking(1L, "test", "Desc", true, null, null, Collections.emptyList());

        when(itemService.getAllByOwner(anyLong())).thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void search_shouldReturnFoundItems() throws Exception {
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("test")
                .build();

        when(itemService.search("test")).thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void search_whenTextBlank_shouldReturnEmptyList() throws Exception {
        when(itemService.search("")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", " "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void addComment_shouldReturnComment() throws Exception {
        CreateCommentDto request = new CreateCommentDto("test");

        CommentDto response = CommentDto.builder()
                .id(1L)
                .text("test")
                .build();

        when(itemService.addComment(anyLong(), anyLong(), any(CreateCommentDto.class))).thenReturn(response);

        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("test"));
    }

    @Test
    void deleteItem_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());
    }
}