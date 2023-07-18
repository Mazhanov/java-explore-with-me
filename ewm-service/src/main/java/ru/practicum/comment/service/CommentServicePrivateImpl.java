package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentState;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.core.exception.ConflictException;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.core.exception.ValidationException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServicePrivateImpl implements CommentServicePrivate {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentFullDto createComment(int userId, int eventId, CommentCreateDto commentCreateDto, LocalDateTime dateTime) {
        Event event = findEventByIdAndCheck(eventId);
        User user = findUserByIdAndCheck(userId);

        Comment newComment = Comment.builder()
                .event(event)
                .author(user)
                .textComment(commentCreateDto.getTextComment())
                .createdOn(dateTime)
                .state(CommentState.PUBLISHED)
                .build();

        return CommentMapper.commentToCommentFullDto(commentRepository.save(newComment));
    }

    @Override
    public CommentFullDto updateComment(int userId, int eventId, int commentId, CommentUpdateDto commentUpdateDto) {
        findEventByIdAndCheck(eventId);
        findUserByIdAndCheck(userId);
        Comment comment = findCommentByIdAndCheck(commentId);

        if (comment.getEvent().getEventId() != eventId) {
            throw new ConflictException("There is no comment in the event");
        }

        if (comment.getAuthor().getUserId() != userId) {
            throw new ValidationException("Comment", "The comment can only be changed by the creator");
        }

        if (comment.getState().equals(CommentState.DELETED)) {
            throw new ConflictException("Comment deleted");
        }

        if (LocalDateTime.now().isAfter(comment.getCreatedOn().plusHours(2))) {
            throw new ConflictException("You can edit comments within two hours");
        }

        comment.setTextComment(commentUpdateDto.getTextComment());

        return CommentMapper.commentToCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(int userId, int commentId) {
        findUserByIdAndCheck(userId);
        Comment comment = findCommentByIdAndCheck(commentId);

        if (comment.getAuthor().getUserId() != userId) {
            throw new ValidationException("Comment", "A comment can only be deleted by the creator");
        }

        comment.setState(CommentState.DELETED);

        commentRepository.save(comment);
    }

    private Comment findCommentByIdAndCheck(int commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ObjectNotFoundException("Comment", commentId));
    }

    private Event findEventByIdAndCheck(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Event", eventId));
    }

    private User findUserByIdAndCheck(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User", userId));
    }
}
