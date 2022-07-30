package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User addUser(User user) {
        return storage.addUser(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add user")
        );
    }

    public User updateUser(int userId, User user) {
        return storage.updateUser(userId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to edit user")
        );
    }

    public User getUser(int userId) {
        return storage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
    }

    public User removeUser(int userId) {
        return storage.removeUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
    }

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }


}
