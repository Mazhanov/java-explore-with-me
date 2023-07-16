package ru.practicum.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(nullable = false, name = "requester_id")
    private User requester;

    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public Request(Event event, User requester, LocalDateTime created, RequestStatus status) {
        this.event = event;
        this.requester = requester;
        this.created = created;
        this.status = status;
    }
}
