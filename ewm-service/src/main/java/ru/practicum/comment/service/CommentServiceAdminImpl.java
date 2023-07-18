package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentState;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.core.exception.ConflictException;
import ru.practicum.core.exception.ObjectNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceAdminImpl implements CommentServiceAdmin {
    private final CommentRepository commentRepository;

    @Override
    public CommentFullDto updateCommentAdmin(int commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = findCommentByIdAndCheck(commentId);

        if (comment.getState().equals(CommentState.DELETED)) {
            throw new ConflictException("Comment deleted");
        }

        comment.setTextComment(commentUpdateDto.getTextComment());

        return CommentMapper.commentToCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentAdmin(int commentId) {
        Comment comment = findCommentByIdAndCheck(commentId);

        comment.setState(CommentState.DELETED);

        commentRepository.save(comment);
    }

    private Comment findCommentByIdAndCheck(int commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ObjectNotFoundException("Comment", commentId));
    }
}
