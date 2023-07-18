package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.EventStateAdminAction;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateAdminRequest extends EventUpdate {
    private EventStateAdminAction stateAction;
}
