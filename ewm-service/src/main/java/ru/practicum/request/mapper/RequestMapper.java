package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@UtilityClass
public class RequestMapper {
    public RequestDto requestToRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getEvent().getEventId(), request.getRequester().getUserId(),
                request.getCreated(), request.getStatus());
    }
}
