package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable(name = "userId") int userId,
                                    @RequestParam(name = "eventId") int eventId) {
        log.info("POST, /users/{userId}/requests, userId = {}, eventId = {}", userId, eventId);
        RequestDto request = requestService.createRequest(userId, eventId);
        log.info("new request {}", request);
        return request;
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        log.info("PATCH, /users/{userId}/requests/{requestId}/cancel, userId = {}, requestId = {}", userId, requestId);
        RequestDto requestDto = requestService.cancelRequest(userId, requestId);
        log.info("Request update {}", requestDto);
        return requestDto;
    }

    @GetMapping
    public List<RequestDto> getAllRequests(@PathVariable int userId) {
        log.info("GET, /users/{userId}/requests, userId = {}", userId);
        List<RequestDto> requestDto = requestService.getAllRequests(userId);
        log.info("requests received = {}", requestDto);
        return requestDto;
    }
}
