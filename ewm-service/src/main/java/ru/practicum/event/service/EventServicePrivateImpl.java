package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.core.exception.ConflictException;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.core.exception.ValidationException;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.utils.EventUtils;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePrivateImpl implements EventServicePrivate {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventUtils eventUtils;

    @Override
    public EventFullDto createEvent(int userId, EventCreateDto event, LocalDateTime now) {
        checkDate(now, event.getEventDate());

        User user = findUserByIdAndCheck(userId);
        Category category = findCategoryByIdAndCheck(event.getCategory());
        Event newEvent = EventMapper.eventCreateToEvent(event, user, category, now);

        return EventMapper.eventToEventFullDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto updateEventOwn(int userId, int eventId, EventUpdateUserRequest eventUpdate,
                                       LocalDateTime now) {
        if (eventUpdate.getEventDate() != null) {
            checkDate(now, eventUpdate.getEventDate());
        }

        Event event = findEventByIdAndCheck(eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("You can only change canceled events or events in the state of waiting for moderation");
        }

        findUserByIdAndCheck(userId);

        if (event.getInitiator().getUserId() != userId) {
            throw new ValidationException("Event", "The events can only be changed by the creator");
        }

        if (eventUpdate.getCategory() != null) {
            Category category = findCategoryByIdAndCheck(eventUpdate.getCategory());
            event.setCategory(category);
        }

        if (eventUpdate.getStateAction() != null) {
            switch (eventUpdate.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
            }
        }

        updateEvent(event, eventUpdate);

        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(eventRepository.save(event));

        eventUtils.addConfirmedRequestsAndViews(List.of(eventFullDto), requestRepository);

        return eventFullDto;
    }

    @Override
    public EventRequestStatusUpdateResult updateStateEventOwn(int userId, int eventId,
                                            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        findUserByIdAndCheck(userId);
        Event event = findEventByIdAndCheck(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("The status can be changed only for applications that are in the waiting state");
        }

        int eventConfirmedRequests = requestRepository.findCountRequests(eventId);

        if (event.getParticipantLimit() != 0 && eventConfirmedRequests == event.getParticipantLimit()) {
            throw new ConflictException("The limit on applications for this event has been reached");
        }

        List<Request> requests = requestRepository.findRequestsForUpdating(
                eventId,
                userId,
                eventRequestStatusUpdateRequest.getRequestIds()
        );

        if (eventRequestStatusUpdateRequest.getStatus() == RequestStatus.REJECTED) {
            requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            requestRepository.saveAll(requests);

            return new EventRequestStatusUpdateResult(
                    Collections.emptyList(),
                    requests
                            .stream()
                            .map(RequestMapper::requestToRequestDto)
                            .collect(Collectors.toList())
            );
        }

        EventRequestStatusUpdateResult eventRequest = new EventRequestStatusUpdateResult(Collections.emptyList(),
                Collections.emptyList());

        List<Request> requestsForSave = new ArrayList<>();

        requests.forEach(request -> {
            if (eventConfirmedRequests < event.getParticipantLimit()) {
                request.setStatus(RequestStatus.CONFIRMED);
                requestsForSave.add(request);
                List<RequestDto> newRequests = new ArrayList<>(eventRequest.getConfirmedRequests());
                newRequests.add(RequestMapper.requestToRequestDto(request));
                eventRequest.setConfirmedRequests(newRequests);
            } else {
                request.setStatus(RequestStatus.REJECTED);

                List<RequestDto> newRequests = new ArrayList<>(eventRequest.getRejectedRequests());
                newRequests.add(RequestMapper.requestToRequestDto(request));
                eventRequest.setRejectedRequests(newRequests);
            }
        });

        requestRepository.saveAll(requestsForSave);

        return eventRequest;
    }

    @Override
    public List<EventShortDto> getEventsOwn(int userId, Pageable pageable) {
        findUserByIdAndCheck(userId);

        List<EventFullDto> events = eventRepository
                .findAllByInitiatorUserId(userId, pageable)
                .stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(Collectors.toList());

        eventUtils.addConfirmedRequestsAndViews(events, requestRepository);

        return events
                .stream()
                .map(EventMapper::eventFullDtoToEventShotDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventOwn(int userId, int eventId) {
        findUserByIdAndCheck(userId);
        Event event = findEventByIdAndCheck(eventId);

        if (event.getInitiator().getUserId() != userId) {
            throw new ValidationException("Event", "The events can only be changed by the creator");
        }

        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(event);

        eventUtils.addConfirmedRequestsAndViews(List.of(eventFullDto), requestRepository);

        return eventFullDto;
    }

    @Override
    public List<RequestDto> getRequestsEventsOwn(int userId, int eventId) {
        findUserByIdAndCheck(userId);
        Event event = findEventByIdAndCheck(eventId);

        if (!event.getInitiator().getUserId().equals(userId)) {
            throw new ConflictException("Only the owner of the event can receive requests");
        }

        List<Request> requests = requestRepository.findByEventEventId(eventId);

        return requests
                .stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }


    private Event findEventByIdAndCheck(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Event", eventId));
    }

    private User findUserByIdAndCheck(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User", userId));
    }

    private Category findCategoryByIdAndCheck(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ObjectNotFoundException("Category", categoryId));
    }

    private void checkDate(LocalDateTime now, LocalDateTime eventDate) {
        if (now.plusHours(2).isAfter(eventDate)) {
            throw new ValidationException("Event", "The events cannot be earlier than two hours from the current moment");
        }
    }

    private void updateEvent(Event event, EventUpdateUserRequest eventUpdate) {
        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }

        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (eventUpdate.getDescription() != null) {
            event.setDescription(eventUpdate.getDescription());
        }

        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(eventUpdate.getEventDate());
        }

        if (eventUpdate.getLocation() != null) {
            event.setLatitude(eventUpdate.getLocation().getLat());
            event.setLongitude(eventUpdate.getLocation().getLon());
        }

        if (eventUpdate.getPaid() != null) {
            event.setPaid(eventUpdate.getPaid());
        }

        if (eventUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }

        if (eventUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdate.getRequestModeration());
        }
    }
}
