package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> addItem(Item item);

    Optional<Item> updateItem(Long itemId, Item item);

    Optional<Item> getItem(Long itemId);

    List<Item> getAllItems(Long userId);

    List<Item> searchItem(String text);
}
