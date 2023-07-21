package ru.practicum.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShotDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

@UtilityClass
public class CommentMapper {
    public CommentFullDto commentToCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .event(eventToEventShortDto(comment.getEvent()))
                .author(userToUserShortDto(comment.getAuthor()))
                .textComment(comment.getTextComment())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public CommentShotDto commentToCommentShotDto(Comment comment) {
        return CommentShotDto.builder()
                .author(userToUserShortDto(comment.getAuthor()))
                .textComment(comment.getTextComment())
                .build();
    }

    private UserShortDto userToUserShortDto(User user) {
        return UserMapper.toUserShortDto(user);
    }

    private EventShortDto eventToEventShortDto(Event event) {
        return EventMapper.eventToEventShotDto(event);
    }
}
