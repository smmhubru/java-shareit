package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;
    @Mock
    private ItemStorage itemStorage;

    @Test
    void addItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getAllItems() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void searchItemByText() {
    }
}