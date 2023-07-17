package ru.practicum.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.core.DateTime;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
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
