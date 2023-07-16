package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.core.DateTime;
import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Integer id;

    private Integer event;

    private Integer requester;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime.DATE_TIME_FORMAT)
    private LocalDateTime created;

    private RequestStatus status;
}
