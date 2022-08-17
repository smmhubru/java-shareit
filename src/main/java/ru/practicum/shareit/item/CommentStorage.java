package ru.practicum.shareit.item;

import java.util.Optional;

public interface CommentStorage {
    Optional<Comment> addComment(Comment comment);
}
