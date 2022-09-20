package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserStorage userStorage;

    @Test
    void addUser() {
        User createUserRequest = new User();
        createUserRequest.setName("foo user");
        createUserRequest.setEmail("foo@mail.ru");
        User user = new User(424242L, createUserRequest.getName(), createUserRequest.getEmail());
        when(userStorage.addUser(any(User.class))).thenReturn(Optional.of(user));
        User created = userService.addUser(createUserRequest);
        assertEquals("foo user", created.getName());
        assertEquals("foo@mail.ru", created.getEmail());
    }


    @Test
    void updateUser() {
        User updateUserRequest = new User();
        updateUserRequest.setName("updated user");
        updateUserRequest.setEmail("updated@foo.mail");
        User user = new User(424242L, updateUserRequest.getName(), updateUserRequest.getEmail());
        when(userStorage.updateUser(Mockito.anyLong(), any(User.class))).thenReturn(Optional.of(user));
        User updated = userService.updateUser(424242L, updateUserRequest);
        assertEquals(updateUserRequest.getName(), updated.getName());
        assertEquals(updateUserRequest.getEmail(), updated.getEmail());
    }

    @Test
    void getUser() {
        User user = new User();
        user.setName("foo user");
        user.setEmail("foo@user.email");
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        User result = userService.getUser(42L);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void removeUser() {
        User user = new User();
        user.setName("removed user");
        user.setEmail("removed@foo.email");
        when(userStorage.removeUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        User result = userService.removeUser(42L);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getAllUsers() {
        User user1 = new User();
        user1.setName("first user");
        user1.setEmail("first@test.user");
        User user2 = new User();
        user2.setName("second user");
        user2.setEmail("second@test.user");
        when(userStorage.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }
}