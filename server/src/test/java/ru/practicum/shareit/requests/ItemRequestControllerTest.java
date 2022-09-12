package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;

    final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    MockMvc mockMvc;

    @Test
    void createItemRequest() throws Exception {
        ItemRequestDto item = new ItemRequestDto();
        item.setId(42L);
        ItemRequest request = new ItemRequest();
        request.setId(item.getId());
        request.setDescription("foo");
        when(itemRequestService.addItemRequest(Mockito.anyLong(),any(ItemRequest.class))).thenReturn(item);
        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", "42")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()));
    }

    @Test
    void getOwnItemRequests() throws Exception {
        List<ItemRequestDto> requests = List.of(new ItemRequestDto(), new ItemRequestDto());
        when(itemRequestService.getOwnItemRequests(Mockito.anyLong())).thenReturn(requests);
        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", "42"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getItemRequestById() throws Exception {
        ItemRequestDto request = new ItemRequestDto();
        request.setId(42L);
        when(itemRequestService.getItemRequestById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(request);
        mockMvc.perform(get("/requests/42")
                .header("X-Sharer-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()));
    }

    @Test
    void getAllItemRequests() throws Exception {
        List<ItemRequestDto> requests = List.of(new ItemRequestDto(), new ItemRequestDto());
        when(itemRequestService.getAllItemRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(requests);
        mockMvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", "42")
                .param("from", "42")
                .param("size", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}