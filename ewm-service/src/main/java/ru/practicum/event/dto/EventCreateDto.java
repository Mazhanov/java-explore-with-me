package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.core.DateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
    @NotNull
    private int category;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @Valid
    @NotNull
    private Location location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private Boolean requestModeration = true; // Нужна ли пре-модерация заявок на участие
}
