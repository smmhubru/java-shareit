package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@Qualifier("persistentUserStorage")
public class PersistentUserStorage implements UserStorage {
    private final UserRepository userRepository;

    @Autowired
    public PersistentUserStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> addUser(User user) {
        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<User> removeUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> userRepository.deleteById(u.getId()));
        return user;
    }

    @Override
    @Transactional
    public Optional<User> updateUser(Long userId, User user) {
        User searchResult = getUser(userId).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "Unable to find user")
        );
        if (user.getName() != null) searchResult.setName(user.getName());
        if (user.getEmail() != null && !isEmailUnique(user)) return Optional.empty();
        if (user.getEmail() != null && isEmailUnique(user)) searchResult.setEmail(user.getEmail());
        return Optional.of(searchResult);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean isEmailUnique(User user) {
        List<User> result = userRepository.findByEmail(user.getEmail());
        return result.size() == 0;
    }
}
