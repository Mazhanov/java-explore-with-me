package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.model.Request;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findByRequesterUserIdAndEventEventId(int requesterUserId, int eventEventId);

    @Query("SELECT count(r) FROM Request r " +
            "WHERE r.event.eventId = :eventId AND r.status = 'CONFIRMED'")
    int findCountRequests(@Param("eventId") int eventId);

    List<Request> findAllByRequesterUserId(int requesterUserId);

    @Query("SELECT count(r) FROM Request r " +
            "WHERE r.event.eventId = :eventId")
    int findCountOfEvent(@Param("eventId") int eventId);

    @Query("SELECT r FROM Request r " +
            "WHERE r.event.eventId = :eventId AND " +
            "r.event.initiator.userId = :initiatorId AND " +
            "r.id in :requestIds " +
            "ORDER BY r.created ASC")
    List<Request> findRequestsForUpdating(
            @Param("eventId") int eventId,
            @Param("initiatorId") int initiatorId,
            @Param("requestIds") List<Integer> requestIds
    );

    List<Request> findByEventEventId(int eventEventId);

    List<Request> findByEventEventIdIn(Collection<Integer> events);


    @Query("SELECT r FROM Request r " +
            "WHERE r.event.eventId IN :eventId AND r.status = 'CONFIRMED'")
    List<Request> findAllConfirmedByEventIdIn(List<Integer> eventId);
}
