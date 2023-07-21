package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;

import java.time.LocalDateTime;

public interface CommentServicePrivate {
    CommentFullDto createComment(int userId, int eventId, CommentCreateDto commentCreateDto, LocalDateTime dateTime);

    CommentFullDto updateComment(int userId, int eventId, int commentId, CommentUpdateDto commentUpdateDto);

    void deleteComment(int userId, int commentId);
}
