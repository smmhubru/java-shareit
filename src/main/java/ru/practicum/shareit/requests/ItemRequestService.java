package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemRequestService(ItemRequestStorage storage, UserStorage userStorage) {
        this.itemRequestStorage = storage;
        this.userStorage = userStorage;
    }

    public ItemRequestDto addItemRequest(Long userId, ItemRequest itemRequest) {
        User requester = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        itemRequest.setRequester(requester);
        itemRequest.setCreatedAt(LocalDateTime.now());
        return itemRequestStorage.addItemRequest(itemRequest).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add item request")
        );
    }

    public List<ItemRequestDto> getOwnItemRequests(Long userId) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return itemRequestStorage.getItemRequestsByUserId(userId);
    }

    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return itemRequestStorage.getItemRequestById(requestId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item request")
        );
    }

    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        return itemRequestStorage.getAllItemRequests(userId, from, size);
    }
}
