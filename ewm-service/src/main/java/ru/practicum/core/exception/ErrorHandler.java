package ru.practicum.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError fieldValidationExceptionHandler(FieldValidationException exception) {
        return new ApiError(
                exception.getMessage(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError notFoundExceptionHandler(ConflictException exception) {
        return new ApiError(
                exception.getMessage(),
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError objectNotFoundExceptionHandler(ObjectNotFoundException exception) {
        return new ApiError(
                exception.getMessage(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOtherUserException(final AccessDeniedException exception) {
        log.error("Exception AccessDeniedException {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemUnavailableException(final ItemUnavailableException exception) {
        log.error("Exception ItemUnavailableException {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }*/

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(
                exception.getMessage(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException exception) {
        return new ApiError(
                exception.getMessage(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        return new ApiError(
                exception.getMessage(),
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }

    /*@ExceptionHandler
    public Map<String, String> handleUnsupportedStatusException(UnsupportedStatusException exception) {
        log.error("Exception UnsupportedStatusException", exception);
        return Map.of("error", exception.getMessage());
    }*/

   /* @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception exception) {
        log.error("500 {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }*/
}
