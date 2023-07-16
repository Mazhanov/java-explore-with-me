package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationServicePublic;
import ru.practicum.core.pagination.PaginationMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
@Slf4j
public class CompilationControllerPublic {
    private final CompilationServicePublic compilationServicePublic;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @Positive @RequestParam(defaultValue = "10") int size) {
        return compilationServicePublic.getAllCompilations(pinned, PaginationMapper.toPageable(from, size));
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable int compilationId) {
        return compilationServicePublic.getCompilationById(compilationId);
    }
}
