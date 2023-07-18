package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.service.CommentServicePrivate;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/users/{userId}/comment")
@AllArgsConstructor
@Slf4j
public class CommentControllerPrivate {
    private final CommentServicePrivate commentServicePrivate;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable(name = "userId") int userId,
                                        @PathVariable(name = "eventId") int eventId,
                                        @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("POST, /users/{userId}/comment, userId = {}, event = {}, commentCreateDto = {}", userId, eventId, commentCreateDto);
        CommentFullDto newComment = commentServicePrivate.createComment(userId, eventId, commentCreateDto, LocalDateTime.now());
        log.info("New comment {}", newComment);
        return newComment;
    }

    @PatchMapping("/{eventId}/{commentId}")
    public CommentFullDto updateComment(@PathVariable(name = "userId") int userId,
                                        @PathVariable(name = "eventId") int eventId,
                                        @PathVariable(name = "commentId") int commentId,
                                        @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        log.info("PATCH, /users/{userId}/comment, userId = {}, eventId = {}, commentId = {}, commentUpdateDto = {}",
                userId, eventId, commentId, commentUpdateDto);
        CommentFullDto updateComment = commentServicePrivate.updateComment(userId, eventId, commentId, commentUpdateDto);
        log.info("Comment update {}", updateComment);
        return updateComment;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int userId, @PathVariable int commentId) {
        log.info("DELETE, /users/{userId}/comment{commentId}, userId = {}, commentId = {}", userId, commentId);
        commentServicePrivate.deleteComment(userId, commentId);
        log.info("removed the comment = {} of user = {}", commentId, userId);
    }
}
