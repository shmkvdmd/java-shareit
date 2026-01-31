package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService requestService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("desc")
                .build();

        ItemRequestDto responseDto = ItemRequestDto.builder()
                .id(1L)
                .description("desc")
                .requestorId(1L)
                .created(LocalDateTime.now())
                .build();

        when(requestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("desc"));
    }

    @Test
    void getOwnRequests_shouldReturnOwnList() throws Exception {
        ItemRequestWithItemsDto dto = ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("desc")
                .items(Collections.emptyList())
                .build();

        when(requestService.getOwnRequests(anyLong())).thenReturn(List.of(dto));

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getAllRequests_shouldReturnOthersList() throws Exception {
        ItemRequestWithItemsDto dto = ItemRequestWithItemsDto.builder()
                .id(2L)
                .description("desc2")
                .items(Collections.emptyList())
                .build();

        when(requestService.getAllRequests(anyLong())).thenReturn(List.of(dto));

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void getById_shouldReturnRequestWithItems() throws Exception {
        ItemRequestWithItemsDto response = ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("desc")
                .items(Collections.emptyList())
                .build();

        when(requestService.getById(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("desc"));
    }
}