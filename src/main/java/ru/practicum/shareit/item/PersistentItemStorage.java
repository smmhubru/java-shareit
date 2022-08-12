package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Qualifier("persistentItemStorage")
@Transactional(readOnly = true)
public class PersistentItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;

    @Autowired
    public PersistentItemStorage(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public Optional<Item> addItem(Item item) {
        return Optional.of(itemRepository.saveAndFlush(item));
    }

    @Override
    @Transactional
    public Optional<Item> updateItem(Long itemId, Item item) {
        Optional<Item> searchItem = itemRepository.findById(itemId);
        if (searchItem.isEmpty()) return Optional.empty();
        if (item.getName() != null) searchItem.get().setName(item.getName());
        if (item.getDescription() != null) searchItem.get().setDescription(item.getDescription());
        if (item.getAvailable() != null) searchItem.get().setAvailable(item.getAvailable());
        if (item.getOwner() != null) searchItem.get().setOwner(item.getOwner());
        return searchItem;
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) return new ArrayList<>();
        List<Item> result = itemRepository.findByNameOrDescriptionContainingAllIgnoreCase(text, text);
        return result.stream().filter(Item::getAvailable).collect(Collectors.toList());
    }
}
