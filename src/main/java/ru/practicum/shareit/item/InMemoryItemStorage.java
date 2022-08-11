package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> storage = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public Optional<Item> addItem(Item item) {
        item.setId(++idCounter);
        storage.put(item.getId(), item);
        return Optional.ofNullable(storage.get(item.getId()));
    }

    @Override
    public Optional<Item> updateItem(Long itemId, Item item) {
        Optional<Item> searchItem = Optional.ofNullable(storage.get(itemId));
        if (searchItem.isEmpty()) return Optional.empty();
        if (item.getName() != null) searchItem.get().setName(item.getName());
        if (item.getDescription() != null) searchItem.get().setDescription(item.getDescription());
        if (item.getAvailable() != null) searchItem.get().setAvailable(item.getAvailable());
        if (item.getOwner() != null) searchItem.get().setOwner(item.getOwner());
        return Optional.ofNullable(storage.put(itemId, searchItem.get()));
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        return Optional.ofNullable(storage.get(itemId));
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return storage.values().stream().filter(item -> item.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return storage.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
