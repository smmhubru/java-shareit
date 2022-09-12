package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {
    private Long id;
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    private String name;
    @NotBlank(message = "Description can't be null")
    @NotNull(message = "Description can't be null")
    private String description;
    @NotNull(message = "Available can't be null")
    private Boolean available;
    private Long requestId;
}
