package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.DateTime;
import ru.practicum.core.pagination.PaginationMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Slf4j
@Validated
public class EventControllerAdmin {
    private final EventServiceAdmin eventServiceAdmin;

    @GetMapping
    public List<EventFullDto> getEventsOwn(@RequestParam(name = "users", required = false) List<Integer> userIds,
                                            @RequestParam(required = false) List<String> eventStates,
                                            @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = DateTime.DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = DateTime.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET, /admin/events, userIds = {}, eventStates = {}, categoryIds = {}, rangeStart = {}, rangeEnd = {}",
                userIds, eventStates, categoryIds, rangeStart, rangeEnd);
        List<EventFullDto> eventFullDtoList = eventServiceAdmin.getEvents(userIds, eventStates, categoryIds, rangeStart,
                rangeEnd, PaginationMapper.toPageable(from, size));
        log.info("get a list of events = {}", eventFullDtoList);
        return eventFullDtoList;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventOwn(@PathVariable int eventId,
                                       @Valid @RequestBody EventUpdateAdminRequest eventUpdate) {
        log.info("PATCH, /admin/events/{eventId}, eventId = {}, eventUpdate = {}", eventId, eventUpdate);
        EventFullDto eventFullDto = eventServiceAdmin.updateEvent(eventId, eventUpdate, LocalDateTime.now());
        log.info("event updated = {}", eventFullDto);
        return eventFullDto;
    }


}