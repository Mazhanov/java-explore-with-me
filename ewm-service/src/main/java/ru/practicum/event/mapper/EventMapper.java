package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
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

@UtilityClass
public class EventMapper {
    public Event eventCreateToEvent(EventCreateDto eventDto, User user, Category category, LocalDateTime dateTime) {
        return Event.builder()
                .category(category)
                .initiator(user)
                .createdOn(dateTime)
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .latitude(eventDto.getLocation().getLat())
                .longitude(eventDto.getLocation().getLon())
                .state(EventState.PENDING)
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .build();
    }

    public EventFullDto eventToEventFullDto(Event event) {
        EventFullDto eventFullDto = EventFullDto.builder()
                .id(event.getEventId())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(toLocation(event.getLatitude(), event.getLongitude()))
                .state(event.getState())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .build();

        return eventFullDto;
    }

    public EventShortDto eventFullDtoToEventShotDto(EventFullDto eventDto) {
        return new EventShortDto(eventDto.getId(), eventDto.getTitle(), eventDto.getAnnotation(),
                eventDto.getCategory(), eventDto.getConfirmedRequests(), eventDto.getEventDate(),
                eventDto.getInitiator(), eventDto.getPaid(), eventDto.getViews());
    }

    public EventShortDto eventToEventShotDto(Event event) {
        return EventShortDto.builder()
                .id(event.getEventId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(categoryToCategoryDro(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(userToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .build();
    }

    private CategoryDto categoryToCategoryDro(Category category) {
        return CategoryMapper.toCategoryDto(category);
    }

    private UserShortDto userToUserShortDto(User user) {
        return UserMapper.toUserShortDto(user);
    }

    private Location toLocation(float lat, float lot) {
        return new Location(lat, lot);
    }
}
