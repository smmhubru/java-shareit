package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public Optional<User> addUser(User user) {
        if (isEmailUnique(user)) {
            user.setId(++idCounter);
            storage.put(user.getId(), user);
            return Optional.ofNullable(storage.get(user.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> removeUser(Long userId) {
        return Optional.ofNullable(storage.remove(userId));
    }

    @Override
    public Optional<User> updateUser(Long userId, User user) {
        User searchResult = getUser(userId).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "Unable to find user")
        );
        if (!isEmailUnique(user)) {
            return Optional.empty();
        }
        if (isEmailUnique(user) && user.getEmail() != null) {
            searchResult.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().equals(searchResult.getName())) {
            searchResult.setName(user.getName());
        }
        storage.put(userId, searchResult);
        return Optional.of(searchResult);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(storage.values());
    }

    private boolean isEmailUnique(User user) {
        return storage.values().stream().noneMatch(u -> u.getEmail().equals(user.getEmail()));
    }
}
