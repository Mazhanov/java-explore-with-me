package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationUpdateRequest;

public interface CompilationServiceAdmin {
    CompilationDto createCompilation(CompilationCreateDto compilationCreateDto);

    CompilationDto updateCompilation(int compilationId, CompilationUpdateRequest updateCompilationDto);

    void deleteCompilation(int compilationId);
}
