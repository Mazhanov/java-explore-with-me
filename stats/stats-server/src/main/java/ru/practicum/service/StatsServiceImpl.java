package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.CreateEndpointHitDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.Repository.StatsRepository;
import ru.practicum.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    StatsRepository statsRepository;

    @Override
    public void createEndpointHit(CreateEndpointHitDto createEndpointHitDto) {
        statsRepository.save(StatsMapper.toEndpointHit(createEndpointHitDto));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> viewStats;
        if (unique) {
            viewStats = statsRepository.getStatsUniqueIp(start, end, uris);
        } else {
            viewStats = statsRepository.getStatsAllIp(start, end, uris);
        }
        return viewStats;
    }
}
