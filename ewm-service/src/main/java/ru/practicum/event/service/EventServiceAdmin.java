package ru.practicum.event.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> getEvents(List<Integer> userIds, List<String> eventStates, List<Integer> categoryIds,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto updateEvent(int eventId, EventUpdateAdminRequest eventUpdate, LocalDateTime now);
}
