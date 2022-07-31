package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> addItem(Item item);
    Optional<Item> updateItem(int itemId, Item item);
    Optional<Item> getItem(int itemId);
    List<Item> getAllItems(int userId);
    List<Item> searchItem(String text);
}
