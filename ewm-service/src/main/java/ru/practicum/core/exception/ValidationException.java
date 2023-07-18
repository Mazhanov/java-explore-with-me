package ru.practicum.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
    private final String field;
    private final String description;
}