package ru.practicum.event.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.*;
import ru.practicum.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServicePrivate {

    EventFullDto createEvent(int userId, EventCreateDto event, LocalDateTime dateTime);

    EventFullDto updateEventOwn(int userId, int eventId, EventUpdateUserRequest event, LocalDateTime dateTime);

    EventRequestStatusUpdateResult updateStateEventOwn(int userId, int eventId,
                                     EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventShortDto> getEventsOwn(int userId, Pageable pageable);

    EventFullDto getEventOwn(int userId, int eventId);

    List<RequestDto> getRequestsEventsOwn(int userId, int eventId);
}
