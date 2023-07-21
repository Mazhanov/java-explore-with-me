package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c " +
            "WHERE c.event.eventId IN :eventId AND c.state = 'PUBLISHED'")
    List<Comment> findAllPublishedByEventIdIn(List<Integer> eventId);
}
