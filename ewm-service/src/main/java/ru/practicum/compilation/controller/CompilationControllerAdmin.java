package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationUpdateRequest;
import ru.practicum.compilation.service.CompilationServiceAdmin;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@AllArgsConstructor
@Slf4j
public class CompilationControllerAdmin {
    private final CompilationServiceAdmin compilationServiceAdmin;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CompilationCreateDto compilationCreateDto) {
        return compilationServiceAdmin.createCompilation(compilationCreateDto);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable int compilationId,
                                            @Valid @RequestBody CompilationUpdateRequest updateCompilationDto) {
        return compilationServiceAdmin.updateCompilation(compilationId, updateCompilationDto);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compilationId) {
        compilationServiceAdmin.deleteCompilation(compilationId);
    }
}
