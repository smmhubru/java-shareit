package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.validator.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Qualifier("persistentItemStorage")
@Transactional(readOnly = true)
public class PersistentItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public PersistentItemStorage(ItemRepository itemRepository,
                                 BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
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
    public Optional<ItemDto> getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) return Optional.empty();
        Optional<Booking> lastBooking = bookingRepository.findFirstByItemAndItemOwnerAndEndBeforeOrderByStartDesc(
                item.get(), item.get().getOwner(), LocalDateTime.now());
        Optional<Booking> nextBooking = bookingRepository.findFirstByItemAndItemOwnerAndStartAfterOrderByStartDesc(
                item.get(), item.get().getOwner(), LocalDateTime.now()
        );
        return Optional.of(ItemMapper.toItemDto(itemRepository.findById(itemId).get(),
                lastBooking.orElse(null), nextBooking.orElse(null)));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<Item> result = itemRepository.findByOwnerId(userId, pageable);
        return result.stream().map(i -> {
            Optional<Booking> lastBooking = bookingRepository.findFirstByItemAndItemOwnerAndEndBeforeOrderByStartDesc(
                    i, i.getOwner(), LocalDateTime.now());
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemAndItemOwnerAndStartAfterOrderByStartDesc(
                    i, i.getOwner(), LocalDateTime.now());
            return ItemMapper.toItemDto(i, lastBooking.orElse(null), nextBooking.orElse(null));
        }).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String text, int from, int size) {
        if (text.isBlank()) return new ArrayList<>();
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<Item> result = itemRepository.findByNameOrDescriptionContainingAllIgnoreCase(text, text, pageable);
        return result.stream().filter(Item::getAvailable).collect(Collectors.toList());
    }
}
