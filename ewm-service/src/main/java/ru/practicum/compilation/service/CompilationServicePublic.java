package ru.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {
    List<CompilationDto> getAllCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(int compilationId);
}
