package ru.practicum.shareit.item;

import java.util.Optional;

public interface ItemStorage {
    Optional<Item> addItem(Item item);
    Optional<Item> updateItem(int itemId);
    Optional<Item> getItem(int itemId);
}
