package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Validated
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
        RequestDto request = requestService.createRequest(userId, eventId);
        log.info("Create request {}", request);
        return request;
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public List<RequestDto> getAllRequests(@PathVariable int userId) {
        return requestService.getAllRequests(userId);
    }
}
