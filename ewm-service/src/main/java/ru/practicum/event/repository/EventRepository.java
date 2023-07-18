package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiatorUserId(int initiatorUserId, Pageable pageable);

    @Query("select a from Event a " +
            "where (:#{#userIds == null} = true or a.initiator.userId in :userIds) " +
            "and (:#{#categories == null} = true or a.category.categoryId in :categories) " +
            "and (:#{#eventStatuses == null} = true or a.state in :eventStatuses) " +
            "and ( " +
            "(:#{#rangeStart != null} = true and :#{#rangeEnd != null} = true and a.eventDate between :rangeStart and :rangeEnd) " +
            "or ( " +
            "(:#{#rangeStart == null} = true or a.eventDate >= :rangeStart) and (:#{#rangeEnd == null} = true or a.eventDate <= :rangeEnd)) " +
            ") " +
            "and (:#{#eventId == null} = true or a.eventId = :eventId)")
    Page<Event> getEventsForAdmin(@Param("userIds") List<Integer> userIds, @Param("eventStatuses") List<EventState> eventStatuses,
                                  @Param("categories") List<Integer> categories, @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd, @Param("eventId") Integer eventId, Pageable page);

    @Query("select a from Event a " +
            "where a.state = 'PUBLISHED' " +
            "and (:#{#categories == null} = true or a.category.categoryId in :categories) " +
            "and (:#{#texts == null} = true " +
            "or (lower(a.annotation) like concat('%', :texts, '%') or lower(a.description) like concat('%', :texts, '%')) " +
            ") " +
            "and (:#{#paid == null} = true or a.paid = :paid) " +
            "and ( " +
            "(:#{#rangeStart != null} = true and :#{#rangeEnd != null} = true and a.eventDate between :rangeStart and :rangeEnd) " +
            "or ( " +
            "(:#{#rangeStart == null} = true or a.eventDate >= :rangeStart) and (:#{#rangeEnd == null} = true or a.eventDate <= :rangeEnd)) " +
            ")"
    )
    Page<Event> getEventsForPublicUsers(@Param("texts") String texts, @Param("categories") List<Integer> categories,
                                        @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);

    Set<Event> findAllByEventIdIn(Set<Integer> eventIds);
}
