package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.validator.OffsetBasedPageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PersistentItemRequestStorage implements ItemRequestStorage {
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public PersistentItemRequestStorage(ItemRequestRepository itemRequestRepository) {
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public Optional<ItemRequestDto> addItemRequest(ItemRequest itemRequest) {
        try {
            return Optional.of(ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByUserId(Long userId) {
        return itemRequestRepository.findByRequesterId(userId)
                .stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ItemRequestDto> getItemRequestById(Long itemRequestId) {
        Optional<ItemRequest> result = itemRequestRepository.findById(itemRequestId);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(ItemRequestMapper.toItemRequestDto(result.get()));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        return itemRequestRepository.findAllByRequesterIdNotOrderByCreatedAtDesc(userId, pageable)
                .stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }
}
