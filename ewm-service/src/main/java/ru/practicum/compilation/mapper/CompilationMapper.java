package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation compilationCreateDtoToCompilation(CompilationCreateDto compilationDto) {
        return new Compilation(compilationDto.getPinned(), compilationDto.getTitle());
    }

    public static CompilationDto compilationToCompilationDto(Compilation compilation) {
        if (compilation.getEvents() != null) {
            return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(),
                    eventToEventShotDto(compilation.getEvents()));
        } else {
            return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(), null);
        }

    }

    private static Set<EventShortDto> eventToEventShotDto(Set<Event> events) {
        return events
                .stream()
                .map(EventMapper::eventToEventShotDto)
                .collect(Collectors.toSet());
    }
}
