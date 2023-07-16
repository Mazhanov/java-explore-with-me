package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.core.DateTime;
import ru.practicum.event.model.EventState;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private int id;
    private CategoryDto category;
    private UserShortDto initiator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;
    private String title;
    private String annotation;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private EventState state;
    private Boolean paid; // Нужно ли оплачивать участие
    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    private Integer confirmedRequests; // Количество одобренных заявок на участие в данном событии
    private Long views; // Количество просмотрев события

    public EventFullDto(int id, CategoryDto category, UserShortDto initiator, LocalDateTime createdOn,
                        LocalDateTime publishedOn, String title, String annotation, String description,
                        LocalDateTime eventDate, Location location, EventState state, Boolean paid,
                        Integer participantLimit, Boolean requestModeration) {
        this.id = id;
        this.category = category;
        this.initiator = initiator;
        this.createdOn = createdOn;
        this.publishedOn = publishedOn;
        this.title = title;
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.state = state;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
    }
}
