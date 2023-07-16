package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.EventStateAction;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateUserRequest extends EventUpdate {
    private EventStateAction stateAction;
}
