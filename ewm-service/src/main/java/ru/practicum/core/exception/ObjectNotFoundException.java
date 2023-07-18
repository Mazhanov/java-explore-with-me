package ru.practicum.core.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String entity, long id) {
        super(String.format("%s with id = %d not found", entity, id));
    }
}
