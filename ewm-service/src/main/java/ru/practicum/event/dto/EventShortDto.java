package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.core.DateTime;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private Integer id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private UserShortDto initiator;
    private Boolean paid;
    private Long views;

    public EventShortDto(Integer id, String title, String annotation, CategoryDto category,
                         LocalDateTime eventDate, UserShortDto initiator, Boolean paid) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.paid = paid;
    }
}
