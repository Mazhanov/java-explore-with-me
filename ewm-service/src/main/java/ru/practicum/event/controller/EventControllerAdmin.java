package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.pagination.PaginationMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Slf4j
public class EventControllerAdmin {
    private final EventServiceAdmin eventServiceAdmin;

    @GetMapping
    public List<EventFullDto> getEventsOwn(@RequestParam(name = "users", required = false) List<Integer> userIds,
                                            @RequestParam(required = false) List<String> eventStates,
                                            @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Controller get");
        return eventServiceAdmin.getEvents(userIds, eventStates, categoryIds, rangeStart, rangeEnd,
                PaginationMapper.toPageable(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventOwn(@PathVariable int eventId,
                                       @Valid @RequestBody EventUpdateAdminRequest eventUpdate) {
        log.info("Controller update");
        return eventServiceAdmin.updateEvent(eventId, eventUpdate, LocalDateTime.now());
    }


}