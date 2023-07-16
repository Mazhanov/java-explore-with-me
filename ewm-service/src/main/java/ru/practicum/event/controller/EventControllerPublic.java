package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
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
public class EventControllerPublic {
    private final EventServicePublic eventServicePublic;

    private final String pattern = "yyyy-MM-dd HH:mm:ss";

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = pattern) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = pattern) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(required = false) EventSort sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {

        return eventServicePublic.getEvents(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                PaginationMapper.toPageable(from, size), request);
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEvent(@PathVariable int eventId, HttpServletRequest request) {
        return eventServicePublic.getEvent(eventId, request);
    }


}