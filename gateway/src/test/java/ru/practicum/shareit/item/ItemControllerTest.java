package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CreateCommentDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void shouldCreateItem() throws Exception {
        ItemDto dto = new ItemDto(null, "test", "test", true, null);
        when(itemClient.create(anyLong(), any())).thenReturn(ResponseEntity.ok(dto));

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateItem() throws Exception {
        ItemDto dto = new ItemDto(1L, "updated", "updated", false, null);
        when(itemClient.update(anyLong(), anyLong(), any())).thenReturn(ResponseEntity.ok(dto));

        mockMvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetItemById() throws Exception {
        when(itemClient.getById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllOwnerItems() throws Exception {
        when(itemClient.getAllByOwner(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSearchItems() throws Exception {
        when(itemClient.search(anyString())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteItem() throws Exception {
        when(itemClient.deleteById(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddComment() throws Exception {
        CreateCommentDto dto = new CreateCommentDto("comment");
        when(itemClient.addComment(anyLong(), anyLong(), any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}