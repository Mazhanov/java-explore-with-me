package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationCreateDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    private Set<Integer> events;

    private Boolean pinned = false;
}
