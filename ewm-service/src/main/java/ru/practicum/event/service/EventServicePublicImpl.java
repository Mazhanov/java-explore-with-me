package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.CreateEndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.core.exception.ValidationException;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.utils.EventUtils;
import ru.practicum.request.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private final EventUtils eventUtils;

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, boolean onlyAvailable, EventSort sort,
                                         Pageable pageable, HttpServletRequest request) {
        addStatistics(request);

        if (!StringUtils.isBlank(text)) {
            text = text.trim().toLowerCase();
        }

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ValidationException("rangeStart and rangeEnd", "the start cannot be later than the end");
            }
        }

        Page<Event> eventPage = eventRepository.getEventsForPublicUsers(text, categoryIds, paid, rangeStart,
                rangeEnd, pageable);
        List<EventFullDto> events = eventPage.getContent()
                .stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(Collectors.toList());

        eventUtils.addConfirmedRequestsAndViews(events, requestRepository, statsClient);

        if (sort == EventSort.VIEWS) {
            events.sort((event1, event2) -> Long.compare(event2.getViews(), event1.getViews()));
        }

        return events
                .stream()
                .map(EventMapper::eventFullDtoToEventShotDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(int eventId, HttpServletRequest request) {
        Event event = findEventByIdAndCheck(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ObjectNotFoundException("events", eventId);
        }

        addStatistics(request);

        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(event);

        eventUtils.addConfirmedRequestsAndViews(List.of(eventFullDto), requestRepository, statsClient);

        return eventFullDto;
    }

    private Event findEventByIdAndCheck(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Event", eventId));
    }

    private void addStatistics(HttpServletRequest request) {
        CreateEndpointHitDto statDto = new CreateEndpointHitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());
        statsClient.post(statDto);
    }
}
