package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentStorage commentStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;

    @Autowired
    public CommentService(CommentStorage commentStorage,
                          UserStorage userStorage,
                          ItemStorage itemStorage,
                          BookingStorage bookingStorage) {
        this.commentStorage = commentStorage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
        this.bookingStorage = bookingStorage;
    }

    public Optional<CommentDto> addComment(Long authorId, Long itemId, Comment comment) {
        User author = userStorage.getUser(authorId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        ItemDto itemDto = itemStorage.getItem(itemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        Item item = ItemMapper.toItem(itemDto);
        if (!bookingStorage.checkUserBookedItemInPast(author, item)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to comment not booked item");
        }
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        Optional<Comment> result = commentStorage.addComment(comment);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(CommentMapper.toCommentDto(result.get()));
    }
}
