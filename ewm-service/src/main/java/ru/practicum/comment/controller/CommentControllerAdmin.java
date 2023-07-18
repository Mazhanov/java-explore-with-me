package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.service.CommentServiceAdmin;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/comment")
@AllArgsConstructor
@Slf4j
public class CommentControllerAdmin {
    private final CommentServiceAdmin commentServiceAdmin;

    @PatchMapping("/{commentId}")
    public CommentFullDto updateComment(@PathVariable(name = "commentId") int commentId,
                                        @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        log.info("PATCH, /admin/comment/{commentId}, commentId = {}, commentUpdateDto = {}", commentId, commentUpdateDto);
        CommentFullDto updateComment = commentServiceAdmin.updateCommentAdmin(commentId, commentUpdateDto);
        log.info("Comment admin update {}", updateComment);
        return updateComment;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int commentId) {
        log.info("DELETE, /admin/comment/{commentId}, commentId = {}", commentId);
        commentServiceAdmin.deleteCommentAdmin(commentId);
        log.info("removed the comment = {} admin", commentId);
    }
}
