package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.core.DateTime;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdate {
    private Integer category;

    @Length(min = 3, max = 120)
    private String title;

    @Length(min = 20, max = 2000)
    private String annotation;

    @Length(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @Valid
    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private Boolean requestModeration = true; // Нужна ли пре-модерация заявок на участие
}
