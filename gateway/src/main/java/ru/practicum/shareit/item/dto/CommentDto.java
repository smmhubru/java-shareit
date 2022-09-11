package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentDto {
    private Long id;
    @NotBlank(message = "Text can't be blank")
    @NotNull(message = "Text can't be null")
    private String text;
}
