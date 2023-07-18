package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationUpdateRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(CompilationCreateDto compilationCreateDto) {
        Compilation compilation = CompilationMapper.compilationCreateDtoToCompilation(compilationCreateDto);

        if (compilationCreateDto.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByEventIdIn(compilationCreateDto.getEvents());

            compilation.setEvents(events);
        }

        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(int compilationId, CompilationUpdateRequest updateCompilationDto) {
        Compilation compilation = findCompilationByIdAndCheck(compilationId);

        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }

        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }

        if (updateCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByEventIdIn(updateCompilationDto.getEvents()));
        }

        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(int compilationId) {
        findCompilationByIdAndCheck(compilationId);

        compilationRepository.deleteById(compilationId);
    }

    private Compilation findCompilationByIdAndCheck(int compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new ObjectNotFoundException("Compilation", compilationId));
    }
}
