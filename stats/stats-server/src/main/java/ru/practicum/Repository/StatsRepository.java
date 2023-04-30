package ru.practicum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStats;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query(" select new ru.practicum.ViewStats(eh.app, eh.uri, count(distinct eh.ip)) from EndpointHit eh " +
            "where (eh.timestamp between :start and :end) and (:#{#uris == null} = true or eh.uri in :uris) " +
            "group by eh.app, eh.uri " +
            "order by count(distinct eh.ip) desc")
    List<ViewStats> getStatsUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(" select new ru.practicum.ViewStats(eh.app, eh.uri, count(eh.ip)) from EndpointHit eh " +
            "where (eh.timestamp between :start and :end) and (:#{#uris == null} = true or eh.uri in :uris) " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStats> getStatsAllIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
