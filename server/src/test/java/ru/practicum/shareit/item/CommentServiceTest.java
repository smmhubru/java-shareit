package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentStorage commentStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;


    @Test
    void addComment() {
        Comment comment = new Comment();
        comment.setText("comment text");
        comment.setAuthor(new User(42L, "username", "user@mail.test"));
        comment.setCreated(LocalDateTime.now());
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemStorage.getItem(Mockito.anyLong())).thenReturn(Optional.of(new ItemDto()));
        when(bookingStorage.checkUserBookedItemInPast(any(User.class), any(Item.class))).thenReturn(true);
        when(commentStorage.addComment(any(Comment.class))).thenReturn(Optional.of(comment));
        Optional<CommentDto> result = commentService.addComment(42L, 42L, new Comment());
        assertEquals(comment.getText(), result.get().getText());
    }
}