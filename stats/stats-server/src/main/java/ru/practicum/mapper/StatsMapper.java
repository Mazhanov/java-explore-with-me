package ru.practicum.mapper;

import ru.practicum.CreateEndpointHitDto;
import ru.practicum.model.EndpointHit;

public class StatsMapper {
    public static EndpointHit toEndpointHit(CreateEndpointHitDto dto) {
        return new EndpointHit(dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());
    }
}