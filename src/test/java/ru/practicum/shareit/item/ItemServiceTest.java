package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;
    @Mock
    private ItemStorage itemStorage;

    @Test
    void addItem() {
        Item itemAddrequest = new Item();
        itemAddrequest.setName("test item");
        itemAddrequest.setAvailable(true);
        Item item = new Item(
                424242L, "test item", "item desc", true, new User(), null);
        when(itemStorage.addItem(any(Item.class))).thenReturn(Optional.of(item));
        Item result = itemService.addItem(itemAddrequest);
        assertEquals(result.getName(), itemAddrequest.getName());
    }

    @Test
    void getItem() {
        ItemDto item = new ItemDto(
                42L, "test name", "test desc", true, new User(),
                null, null, null, null);
        when(itemStorage.getItem(Mockito.anyLong())).thenReturn(Optional.of(item));
        ItemDto result = itemService.getItem(42L);
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void getAllItems() {
        ItemDto item1 = new ItemDto(
                42L, "test name", "test desc", true, new User(),
                null, null, null, null);
        ItemDto item2 = new ItemDto(
                43L, "test2 name", "test2 desc", true, new User(),
                null, null, null, null);
        when(itemStorage.getAllItems(Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(item1, item2));
        List<ItemDto> result = itemService.getAllItems(42L, 42, 42);
        assertEquals(2, result.size());
    }

    @Test
    void updateItem() {
        Item item = new Item();
        item.setName("test item");
        item.setAvailable(true);
        when(itemStorage.updateItem(Mockito.anyLong(), any(Item.class))).thenReturn(Optional.of(item));
        Item result = itemService.updateItem(42L, new Item());
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void searchItemByText() {
        Item item1 = new Item();
        Item item2 = new Item();
        when(itemStorage.searchItem(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(item1, item2));
        List<Item> result = itemService.searchItemByText("foo", 42, 42);
        assertEquals(2, result.size());
    }
}