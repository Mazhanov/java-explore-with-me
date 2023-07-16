package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.core.pagination.PaginationMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@AllArgsConstructor
@Slf4j
public class CategoryControllerPublic {
    CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.getCategories(PaginationMapper.toPageable(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getAllCategoryById(@PathVariable int catId) {
        return categoryService.getCategoryById(catId);
    }
}
