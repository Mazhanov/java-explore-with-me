package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.core.exception.ConflictException;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.event.dto.EventFullDto;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventUtils eventUtils;

    @Override
    public RequestDto createRequest(int userId, int eventId) {
        User user = findUserByIdAndCheck(userId);
        Event event = findEventByIdAndCheck(eventId);

        requestRepository.findByRequesterUserIdAndEventEventId(userId, eventId).ifPresent(request -> {
            throw new ConflictException("You cannot add a repeat request");
        });

        if (event.getInitiator().getUserId() == userId) {
            throw new ConflictException("The initiator of the event cannot add a request to participate in his event");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("You cannot participate in an unpublished event");
        }

        int countRequests = requestRepository.findCountRequests(eventId);

        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(event);
        eventUtils.addConfirmedRequestsAndViews(List.of(eventFullDto), requestRepository);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == countRequests) {
            throw new ConflictException("The event has reached the limit of participation requests");
        }

        RequestStatus status;

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }
        log.info("event {}", event);
        log.info("status {}", status);
        Request request = new Request(event, user, LocalDateTime.now(), status);

        return RequestMapper.requestToRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto cancelRequest(int userId, int requestId) {
        Request request = findRequestByIdAndCheck(requestId);
        findUserByIdAndCheck(userId);

        if (!request.getRequester().getUserId().equals(userId)) {
            throw new ObjectNotFoundException("request", requestId);
        }

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.requestToRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getAllRequests(int userId) {
        findUserByIdAndCheck(userId);

        List<Request> requests = requestRepository.findAllByRequesterUserId(userId);

        return requests
                .stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    private Request findRequestByIdAndCheck(int requestId) {
        return requestRepository.findById(requestId).orElseThrow(() ->
                new ObjectNotFoundException("Request", requestId));
    }

    private Event findEventByIdAndCheck(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Event", eventId));
    }

    private User findUserByIdAndCheck(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User", userId));
    }
}
