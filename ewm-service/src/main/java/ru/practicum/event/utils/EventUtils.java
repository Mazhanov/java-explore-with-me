package ru.practicum.event.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.core.DateTime;
import ru.practicum.core.StatRestClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EventUtils {
    private final StatRestClient statRestClient;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DateTime.DATE_TIME_FORMAT);

    public void addConfirmedRequestsAndViews(List<EventFullDto> evens, RequestRepository requestRepository,
                                             StatsClient statsClient) {
        addConfirmedRequests(evens, requestRepository);
        addViews(evens);
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
        log.info("LocalDateTime start {}", start);
        StringBuilder builder = new StringBuilder();
        for (Integer uri : eventsId) {
            builder.append("/events/" + uri + ",");
        }
        log.info(" StringBuilder builder {}", builder);
        ResponseEntity<Object[]> response = statRestClient.get(start, LocalDateTime.now(), true, builder.toString());
        log.info(" SResponseEntity<Object[]> response {}", response);
        Map<Long, Long> resultMap = new HashMap<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object[] object = response.getBody();
            log.info("Object[] object {}", object);
            List<ViewStats> collect = Arrays.stream(object).map(o -> objectMapper.convertValue(o, ViewStats.class))
                    .filter(dto -> dto.getApp().equals("ewm-service"))
                    .collect(Collectors.toList());
            log.info("List<ViewStats> collect {}", collect);
            for (ViewStats dto : collect) {
                long id = NumberUtils.toLong(StringUtils.substringAfterLast(dto.getUri(), "/"));
                if (id > 0) {
                    resultMap.put(id, dto.getHits());
                }
            }
        }
        log.info("Map<Long, Long> resultMap {}", resultMap);
        if (resultMap.size() > 0) {
            for (EventFullDto event : events) {
                event.setViews(resultMap.getOrDefault((long) event.getId(), 0L));
            }
        }
    }
}