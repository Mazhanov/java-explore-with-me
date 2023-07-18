package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.DateTime;
import ru.practicum.core.pagination.PaginationMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@AllArgsConstructor
@Slf4j
@Validated
public class EventControllerPublic {
    private final EventServicePublic eventServicePublic;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = DateTime.DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = DateTime.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(required = false) EventSort sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info("GET, /events, text = {}, categoryIds = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, sort = {}",
                text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        List<EventShortDto> eventShortDto = eventServicePublic.getEvents(text, categoryIds, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, PaginationMapper.toPageable(from, size), request);
        log.info("received a list of events = {}", eventShortDto);
        return eventShortDto;
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEvent(@PathVariable int eventId, HttpServletRequest request) {
        log.info("GET, /events, eventId = {}", eventId);
        EventFullDto eventFullDto = eventServicePublic.getEvent(eventId, request);
        log.info("event received = {}", eventFullDto);
        return eventFullDto;
    }


}