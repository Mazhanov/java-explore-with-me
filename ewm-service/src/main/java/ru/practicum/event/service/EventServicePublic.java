package ru.practicum.event.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventServicePublic {

    List<EventShortDto> getEvents(String text, List<Integer> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, boolean onlyAvailable, EventSort sort, Pageable pageable,
                                     HttpServletRequest request);

    EventFullDto getEvent(int eventId, HttpServletRequest request);
}
