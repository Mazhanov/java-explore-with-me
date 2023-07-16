package ru.practicum.event.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.Location;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event eventCreateToEvent(EventCreateDto eventDto, User user, Category category, LocalDateTime dateTime) {
        return new Event(category, user, dateTime, eventDto.getTitle(), eventDto.getAnnotation(),
                eventDto.getDescription(), eventDto.getEventDate(), eventDto.getLocation().getLat(),
                eventDto.getLocation().getLon(), EventState.PENDING, eventDto.getPaid(),
                eventDto.getParticipantLimit(), eventDto.getRequestModeration());
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        return new EventFullDto(event.getEventId(), CategoryMapper.toCategoryDto(event.getCategory()),
                UserMapper.toUserShortDto(event.getInitiator()), event.getCreatedOn(), event.getPublishedOn(),
                event.getTitle(), event.getAnnotation(), event.getDescription(), event.getEventDate(),
                toLocation(event.getLatitude(), event.getLongitude()), event.getState(), event.getPaid(),
                event.getParticipantLimit(), event.getRequestModeration());
    }

    public static EventShortDto eventFullDtoToEventShotDto(EventFullDto eventDto) {
        return new EventShortDto(eventDto.getId(), eventDto.getTitle(), eventDto.getAnnotation(),
                eventDto.getCategory(), eventDto.getConfirmedRequests(), eventDto.getEventDate(),
                eventDto.getInitiator(), eventDto.getPaid(), eventDto.getViews());
    }

    public static EventShortDto eventToEventShotDto(Event event) {
        return new EventShortDto(event.getEventId(), event.getTitle(), event.getAnnotation(),
                categoryToCategoryDro(event.getCategory()), event.getEventDate(),
                userToUserShortDto(event.getInitiator()), event.getPaid());
    }

    private static CategoryDto categoryToCategoryDro(Category category) {
        return CategoryMapper.toCategoryDto(category);
    }

    private static UserShortDto userToUserShortDto(User user) {
        return UserMapper.toUserShortDto(user);
    }

    private static Location toLocation(float lat, float lot) {
        return new Location(lat, lot);
    }
}
