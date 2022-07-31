package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryItemStorage implements ItemStorage{
    private final Map<Integer, Item> storage = new HashMap<>();
    private int idCounter = 0;

    @Override
    public Optional<Item> addItem(Item item) {
        item.setId(++idCounter);
        storage.put(item.getId(), item);
        System.out.println(item);
        return Optional.ofNullable(storage.get(item.getId()));
    }

    @Override
    public Optional<Item> updateItem(int itemId) {
        return Optional.empty();
    }

    @Override
    public Optional<Item> getItem(int itemId) {
        return Optional.ofNullable(storage.get(itemId));
    }
}
