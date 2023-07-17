package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.pagination.PaginationMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventServicePrivate;
import ru.practicum.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@AllArgsConstructor
@Slf4j
@Validated
public class EventControllerPrivate {
    private final EventServicePrivate eventServicePrivate;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int userId,
                                    @Valid @RequestBody EventCreateDto event) {
        log.info("POST, /users/{userId}/events, userId = {}, event = {}", userId, event);
        EventFullDto newEvent = eventServicePrivate.createEvent(userId, event, LocalDateTime.now());
        log.info("new event {}", newEvent);
        return newEvent;
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateEventOwn(@PathVariable int userId,
                                       @PathVariable int eventId,
                                       @Valid @RequestBody EventUpdateUserRequest event) {
        log.info("PATCH, /users/{userId}/events/{eventId}, userId = {}, eventId = {}, event = {}", userId, eventId, event);
        EventFullDto updateEvent = eventServicePrivate.updateEventOwn(userId, eventId, event, LocalDateTime.now());
        log.info("Event update {}", updateEvent);
        return updateEvent;
    }

    @PatchMapping(path = "/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStateEventOwn(@PathVariable int userId,
                                       @PathVariable int eventId,
                                       @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH, /users/{userId}/events/{eventId}/requests, userId = {}, eventId = {}, " +
                "eventRequestStatusUpdateRequest = {}", userId, eventId, eventRequestStatusUpdateRequest);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult =
                eventServicePrivate.updateStateEventOwn(userId, eventId, eventRequestStatusUpdateRequest);
        log.info("Event update status requests {}", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }

    @GetMapping
    public List<EventShortDto> getEventsOwn(@PathVariable int userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET, /users/{userId}/events, userId = {}", userId);
        List<EventShortDto> eventShortDtos = eventServicePrivate.getEventsOwn(userId, PaginationMapper.toPageable(from, size));
        log.info("get a list of events = {}", eventShortDtos);
        return eventShortDtos;
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEventOwn(@PathVariable int userId,
                                    @PathVariable int eventId) {
        log.info("GET, /users/{userId}/events/{eventId}, userId = {}, eventId = {}", userId, eventId);
        EventFullDto eventFullDto = eventServicePrivate.getEventOwn(userId, eventId);
        log.info("event received = {}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<RequestDto> getRequestsEventsOwn(@PathVariable int userId,
                                                 @PathVariable int eventId) {
        log.info("GET, /users/{userId}/events//{eventId}/requests, userId = {}, eventId = {}", userId, eventId);
        List<RequestDto> requestDtos = eventServicePrivate.getRequestsEventsOwn(userId, eventId);
        log.info("requests received = {}", requestDtos);
        return requestDtos;
    }
}
