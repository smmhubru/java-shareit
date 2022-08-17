package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersistentCommentStorage implements CommentStorage {
    private final CommentRepository commentRepository;

    @Autowired
    public PersistentCommentStorage(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Comment> addComment(Comment comment) {
        return Optional.of(commentRepository.save(comment));
    }
}
