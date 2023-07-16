package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@AllArgsConstructor
@Slf4j
public class CategoryControllerAdmin {
    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        return categoryService.createCategory(categoryCreateDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable int catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.changeCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int catId) {
        categoryService.removeCategory(catId);
    }
}
