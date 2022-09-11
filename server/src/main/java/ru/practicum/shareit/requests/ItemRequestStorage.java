package ru.practicum.shareit.requests;

import java.util.List;
import java.util.Optional;

public interface ItemRequestStorage {
    Optional<ItemRequestDto> addItemRequest(ItemRequest itemRequest);

    List<ItemRequestDto> getItemRequestsByUserId(Long userId);

    Optional<ItemRequestDto> getItemRequestById(Long itemRequestId);

    List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size);
}
