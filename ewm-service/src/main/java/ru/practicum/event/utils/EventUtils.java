package ru.practicum.event.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ViewStats;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.core.StatRestClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EventUtils {
    private final StatRestClient statRestClient;
    private final CommentRepository commentRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void addConfirmedRequestsAndViews(List<EventFullDto> events, RequestRepository requestRepository) {
        addConfirmedRequests(events, requestRepository);
        addViews(events);
        addComments(events);
    }

    private void addConfirmedRequests(List<EventFullDto> events, RequestRepository requestRepository) {
        if (events == null || events.isEmpty()) {
            return;
        }

        Map<Integer, Integer> requestsCountMap = new HashMap<>();

        List<Request> requests = requestRepository.findAllConfirmedByEventIdIn(events
                .stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList())
        );

        requests.forEach(request -> {
            int eventId = request.getEvent().getEventId();

            if (!requestsCountMap.containsKey(eventId)) {
                requestsCountMap.put(eventId, 0);
            }

            requestsCountMap.put(eventId, requestsCountMap.get(eventId) + 1);

        });

        events.forEach(event -> {
            if (requestsCountMap.containsKey(event.getId())) {
                event.setConfirmedRequests(requestsCountMap.get(event.getId()));
            }

            if (event.getConfirmedRequests() == null) {
                event.setConfirmedRequests(0);
            }
        });
    }

    private void addViews(List<EventFullDto> events) {
        Set<Integer> eventsId = events
                .stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toSet());

        LocalDateTime start = events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .map(EventFullDto::getPublishedOn)
                .min(LocalDateTime::compareTo).orElseGet(LocalDateTime::now);

        StringBuilder builder = new StringBuilder();
        for (Integer uri : eventsId) {
            builder.append("/events/" + uri + ",");
        }

        ResponseEntity<Object[]> response = statRestClient.get(start, LocalDateTime.now(), true, builder.toString());

        Map<Long, Long> resultMap = new HashMap<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object[] object = response.getBody();

            List<ViewStats> collect = Arrays.stream(object).map(o -> objectMapper.convertValue(o, ViewStats.class))
                    .filter(dto -> dto.getApp().equals("ewm-service"))
                    .collect(Collectors.toList());

            for (ViewStats dto : collect) {
                long id = NumberUtils.toLong(StringUtils.substringAfterLast(dto.getUri(), "/"));
                if (id > 0) {
                    resultMap.put(id, dto.getHits());
                }
            }
        }

        if (resultMap.size() > 0) {
            for (EventFullDto event : events) {
                event.setViews(resultMap.getOrDefault((long) event.getId(), 0L));
            }
        }
    }

    private void addComments(List<EventFullDto> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        List<Comment> comments = commentRepository.findAllPublishedByEventIdIn(events
                .stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList())
        );

        Map<Integer, List<Comment>> commentsMap = new HashMap<>();

        comments.forEach(comment -> {
            int eventId = comment.getEvent().getEventId();

            if (!commentsMap.containsKey(eventId)) {
                commentsMap.put(eventId, new ArrayList<>());
            }

            commentsMap.get(eventId).add(comment);
        });

        events.forEach(event -> {
            if (commentsMap.containsKey(event.getId())) {
                event.setComments(commentsMap.get(event.getId())
                        .stream()
                        .map(CommentMapper::commentToCommentShotDto)
                        .collect(Collectors.toList()));
            }
        });
    }
}
