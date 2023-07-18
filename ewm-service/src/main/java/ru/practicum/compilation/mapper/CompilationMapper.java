package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation compilationCreateDtoToCompilation(CompilationCreateDto compilationDto) {
        return Compilation.builder()
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public CompilationDto compilationToCompilationDto(Compilation compilation) {
        if (compilation.getEvents() != null) {
            return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(),
                    eventToEventShotDto(compilation.getEvents()));
        } else {
            return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(), null);
        }

    }

    private Set<EventShortDto> eventToEventShotDto(Set<Event> events) {
        return events
                .stream()
                .map(EventMapper::eventToEventShotDto)
                .collect(Collectors.toSet());
    }
}
