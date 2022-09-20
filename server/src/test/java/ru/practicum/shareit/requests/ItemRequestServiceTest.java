package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @InjectMocks
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestStorage itemRequestStorage;
    @Mock
    private UserStorage userStorage;

    @Test
    void addItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequesterId(42L);
        when(userStorage.getUser(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(42L, "username", "user@t.ru")));
        when(itemRequestStorage.addItemRequest(any(ItemRequest.class))).thenReturn(Optional.of(itemRequestDto));
        ItemRequestDto result = itemRequestService.addItemRequest(42L, new ItemRequest());
        assertEquals(itemRequestDto.getRequesterId(), result.getRequesterId());
    }

    @Test
    void getOwnItemRequests() {
        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setRequesterId(42L);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setRequesterId(43L);
        when(userStorage.getUser(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(42L, "testuser", "user@m.ru")));
        when(itemRequestStorage.getItemRequestsByUserId(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestDto1, itemRequestDto2));
        List<ItemRequestDto> result = itemRequestService.getOwnItemRequests(42L);
        assertEquals(2, result.size());
    }

    @Test
    void getItemRequestById() {
        when(userStorage.getUser(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(42L, "testu", "test@user.ru")));
        when(itemRequestStorage.getItemRequestById(Mockito.anyLong())).thenReturn(Optional.of(new ItemRequestDto()));
        ItemRequestDto result = itemRequestService.getItemRequestById(42L, 42L);
        assertInstanceOf(ItemRequestDto.class, result);
    }

    @Test
    void getAllItemRequests() {
        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        ItemRequestDto itemRequestDto2 = new ItemRequestDto();

    }
}