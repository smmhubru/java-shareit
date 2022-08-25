package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> addItem(Item item);

    Optional<Item> updateItem(Long itemId, Item item);

    Optional<ItemDto> getItem(Long itemId);

    List<ItemDto> getAllItems(Long userId);

    List<Item> searchItem(String text);
}
