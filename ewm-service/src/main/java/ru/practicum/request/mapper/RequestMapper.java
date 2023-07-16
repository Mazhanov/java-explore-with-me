package ru.practicum.request.mapper;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

public class RequestMapper {
    public static RequestDto requestToRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getEvent().getEventId(), request.getRequester().getUserId(),
                request.getCreated(), request.getStatus());
    }
}
