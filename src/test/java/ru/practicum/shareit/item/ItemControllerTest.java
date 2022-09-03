package ru.practicum.shareit.item;

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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    MockMvc mockMvc;

    @Test
    void createItem() throws Exception {
        Item request = new Item();
        request.setName("test item");
        request.setDescription("test desc");
        request.setAvailable(true);
        request.setId(42L);
        when(userService.getUser(Mockito.anyLong())).thenReturn(new User());
        when(itemService.addItem(any(Item.class))).thenReturn(request);
        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", "42")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()));
    }

    @Test
    void getAllItems() throws Exception {
        List<ItemDto> items = List.of(new ItemDto(), new ItemDto());
        when(itemService.getAllItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(items);
        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", "42")
                .param("from", "42")
                .param("size", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getItem() throws Exception {
        ItemDto item = new ItemDto();
        item.setId(42L);
        User owner = new User();
        owner.setId(42L);
        item.setOwner(owner);
        when(itemService.getItem(Mockito.anyLong())).thenReturn(item);
        mockMvc.perform(get("/items/42")
                .header("X-Sharer-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()));
    }

    @Test
    void searchItem() throws Exception {
        List<Item> items = List.of(new Item(), new Item());
        when(itemService.searchItemByText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(items);
        mockMvc.perform(get("/items/search")
                .header("X-Sharer-User-Id", "42")
                .param("text", "foo")
                .param("from", "42")
                .param("size", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto item = new ItemDto();
        item.setId(42L);
        User owner = new User();
        owner.setId(42L);
        item.setOwner(owner);
        Item request = new Item();
        request.setName("test item");
        request.setDescription("test desc");
        request.setAvailable(true);
        request.setId(42L);
        when(itemService.getItem(Mockito.anyLong())).thenReturn(item);
        when(itemService.updateItem(Mockito.anyLong(), any(Item.class))).thenReturn(request);
        mockMvc.perform(patch("/items/42")
                .header("X-Sharer-User-Id", "42")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()));
    }

    @Test
    void addComment() throws Exception {
        CommentDto comment = new CommentDto(42L, "foo", "author", LocalDateTime.now());
        Comment request = new Comment();
        request.setText("foo");
        request.setId(42L);
        when(commentService.addComment(Mockito.anyLong(), Mockito.anyLong(), any(Comment.class)))
                .thenReturn(Optional.of(comment));
        mockMvc.perform(post("/items/42/comment")
                .header("X-Sharer-User-Id", "42")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()));
    }
}