package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.CreateEndpointHitDto;
import ru.practicum.service.StatsService;
import ru.practicum.ViewStats;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndpointHit(@Valid @RequestBody CreateEndpointHitDto createEndpointHitDto) {
        statsService.createEndpointHit(createEndpointHitDto);
        log.info("return list stats");
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        List<ViewStats> viewStats = statsService.getStats(start, end, uris, unique);
        log.info("return list stats");
        return viewStats;
    }
}