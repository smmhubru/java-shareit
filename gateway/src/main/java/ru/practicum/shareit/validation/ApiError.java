package ru.practicum.shareit.validation;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
public class ApiError {

    private final HttpStatus status;
    private final String error;
    private final List<String> errors;

    public ApiError(HttpStatus status, String error, List<String> errors) {
        super();
        this.status = status;
        this.error = error;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String error, String errors) {
        super();
        this.status = status;
        this.error = error;
        this.errors = Arrays.asList(errors);
    }
}