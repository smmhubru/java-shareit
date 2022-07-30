package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

/**
 * Interface to interact with User Storage. Includes standard CRUD operations.
 */
public interface UserStorage {
    Optional<User> addUser(User user);
    Optional<User> removeUser(int userId);
    Optional<User> updateUser(int userId, User user);
    Optional<User> getUser(int userId);
    List<User> getAllUsers();
}
