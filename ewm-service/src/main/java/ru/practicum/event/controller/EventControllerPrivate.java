package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class EventControllerPrivate {
    private final EventServicePrivate eventServicePrivate;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int userId,
                                    @Valid @RequestBody EventCreateDto event) {
        EventFullDto newEvent = eventServicePrivate.createEvent(userId, event, LocalDateTime.now());
        log.info("Create Event {}", newEvent);
        return newEvent;
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateEventOwn(@PathVariable int userId,
                                       @PathVariable int eventId,
                                       @Valid @RequestBody EventUpdateUserRequest event) {
        EventFullDto updateEvent = eventServicePrivate.updateEventOwn(userId, eventId, event, LocalDateTime.now());
        log.info("Event update {}", updateEvent);
        return updateEvent;
    }

    @PatchMapping(path = "/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStateEventOwn(@PathVariable int userId,
                                       @PathVariable int eventId,
                                       @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return eventServicePrivate.updateStateEventOwn(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping
    public List<EventShortDto> getEventsOwn(@PathVariable int userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        return eventServicePrivate.getEventsOwn(userId, PaginationMapper.toPageable(from, size));
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEventOwn(@PathVariable int userId,
                                    @PathVariable int eventId) {
        return eventServicePrivate.getEventOwn(userId, eventId);
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<RequestDto> getRequestsEventsOwn(@PathVariable int userId,
                                                 @PathVariable int eventId) {
        return eventServicePrivate.getRequestsEventsOwn(userId, eventId);
    }
}
