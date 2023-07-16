package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateRequest {
    @Size(min = 1, max = 50)
    private String title;

    private Boolean pinned;

    private Set<Integer> events;
}
