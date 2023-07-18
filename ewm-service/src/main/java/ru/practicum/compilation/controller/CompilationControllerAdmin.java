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
        log.info("POST, /admin/compilations, compilationCreateDto {}", compilationCreateDto);
        CompilationDto compilationDto = compilationServiceAdmin.createCompilation(compilationCreateDto);
        log.info("new compilation {}", compilationDto);
        return compilationDto;
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable int compilationId,
                                            @Valid @RequestBody CompilationUpdateRequest updateCompilationDto) {
        log.info("PATCH, /admin/compilations/{compilationId}, compilationId = {}, updateCompilationDto {}",
                compilationId, updateCompilationDto);
        CompilationDto compilationDto = compilationServiceAdmin.updateCompilation(compilationId, updateCompilationDto);
        log.info("compilation updated {}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compilationId) {
        log.info("DELETE, /admin/compilations/{compilationId}, compilationId = {}", compilationId);
        compilationServiceAdmin.deleteCompilation(compilationId);
        log.info("deleting a compilation = {} successfully", compilationId);
    }
}
