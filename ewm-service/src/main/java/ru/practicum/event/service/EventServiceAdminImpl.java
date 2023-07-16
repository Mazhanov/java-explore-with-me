package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.core.exception.ConflictException;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.core.exception.ValidationException;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventUpdateAdminRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.EventStateAdminAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.utils.EventUtils;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final EventUtils eventUtils;

    @Override
    public List<EventFullDto> getEvents(List<Integer> userIds, List<String> eventStates, List<Integer> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<EventState> eventState = null;

        if (!CollectionUtils.isEmpty(eventStates)) {
            eventState = new ArrayList<>();
            for (String state : eventStates) {
                eventState.add(EventState.from(state).orElseThrow(() -> new IllegalArgumentException("Failed to convert " +
                        "value of type java.lang.String to required type EventStatus; nested exception is " +
                        "IllegalArgumentException: For input string: " + state)));
            }
        }

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ConflictException();
            }
        }

        Page<Event> eventsPage = eventRepository.getEventsForAdmin(userIds, eventState, categoryIds, rangeStart, rangeEnd, null, pageable);
        List<EventFullDto> events = eventsPage.getContent()
                .stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(Collectors.toList());


        eventUtils.addConfirmedRequestsAndViews(events, requestRepository);

        return events;
    }

    @Override
    public EventFullDto updateEvent(int eventId, EventUpdateAdminRequest eventUpdate, LocalDateTime now) {
        Event event = findEventByIdAndCheck(eventId);

        checkDate(now, event.getEventDate());

        if (eventUpdate.getEventDate() != null) {
            if ((now.plusHours(2)).isAfter(eventUpdate.getEventDate())) {
                throw new ValidationException("Event", "The events cannot be earlier than two hours from the current moment");
            }

            if (eventUpdate.getEventDate() != null) {
                event.setEventDate(eventUpdate.getEventDate());
            }
        }

        if (eventUpdate.getCategory() != null) {
            Category category = findCategoryByIdAndCheck(eventUpdate.getCategory());
            event.setCategory(category);
        }

        if (eventUpdate.getStateAction() != null) {

            if (event.getState() != EventState.PENDING) {
                throw new ConflictException();
            }

            if (eventUpdate.getStateAction().equals(EventStateAdminAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(now);
            } else {
                event.setState(EventState.CANCELED);
            }
        }

        updateEvent(event, eventUpdate);
        log.info("4");
        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(eventRepository.save(event));
        log.info("5");
        eventUtils.addConfirmedRequestsAndViews(List.of(eventFullDto), requestRepository);
        log.info("6");
        return eventFullDto;
    }

    private Event findEventByIdAndCheck(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Event", eventId));
    }

    private Category findCategoryByIdAndCheck(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ObjectNotFoundException("Category", categoryId));
    }

    private void checkDate(LocalDateTime now, LocalDateTime eventDate) {
        if ((now.plusHours(1)).isAfter(eventDate)) {
            throw new ValidationException("Event", "The events cannot be earlier than two hours from the current moment");
        }
    }

    private void updateEvent(Event event, EventUpdateAdminRequest eventUpdate) {
        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }

        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (eventUpdate.getDescription() != null) {
            event.setDescription(eventUpdate.getDescription());
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
