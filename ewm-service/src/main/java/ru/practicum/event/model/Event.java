package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.core.DateTime;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(nullable = false, name = "initiator_id")
    private User initiator;

    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    private String title;
    private String annotation;
    private String description;

    @Column(name = "event_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private Float latitude; // Широта
    private Float longitude; // Долгота

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EventState state;

    private Boolean paid; // Нужно ли оплачивать участие

    @Column(name = "participant_limit")
    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @Column(name = "request_moderation")
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    public Event(Category category, User initiator, LocalDateTime createdOn, String title,
                 String annotation, String description, LocalDateTime eventDate, Float latitude, Float longitude,
                 EventState state, Boolean paid, Integer participantLimit, Boolean requestModeration) {
        this.category = category;
        this.initiator = initiator;
        this.createdOn = createdOn;
        this.publishedOn = null;
        this.title = title;
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
    }
}
