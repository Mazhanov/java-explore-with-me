package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(int userId, int eventId);

    RequestDto cancelRequest(int userId, int requestId);

    List<RequestDto> getAllRequests(int userId);
}
