package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.core.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;

        if (pinned == null || pinned.equals(false)) {
            compilations = compilationRepository.findAll(pageable).toList();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        }

        return compilations
                .stream()
                .map(CompilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(int compilationId) {
        Compilation compilation = findCompilationByIdAndCheck(compilationId);

        return CompilationMapper.compilationToCompilationDto(compilation);
    }

    private Compilation findCompilationByIdAndCheck(int compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new ObjectNotFoundException("Compilation", compilationId));
    }
}
