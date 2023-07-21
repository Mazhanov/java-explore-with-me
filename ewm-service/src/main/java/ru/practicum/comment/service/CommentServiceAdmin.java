package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;

public interface CommentServiceAdmin {
    CommentFullDto updateCommentAdmin(int commentId, CommentUpdateDto commentUpdateDto);

    void deleteCommentAdmin(int commentId);
}
