package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class CategoryDto {
    private Integer id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
