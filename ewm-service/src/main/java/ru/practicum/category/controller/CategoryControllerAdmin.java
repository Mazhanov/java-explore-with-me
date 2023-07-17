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
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        log.info("POST, /admin/categories, categoryCreateDto {}", categoryCreateDto);
        CategoryDto categoryDto = categoryService.createCategory(categoryCreateDto);
        log.info("new category {}", categoryDto);
        return categoryDto;
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable int catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("PATCH, /admin/categories/{catId}, catId = {}, categoryDto {}", catId, categoryDto);
        CategoryDto category = categoryService.changeCategory(catId, categoryDto);
        log.info("category updated {}", category);
        return category;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int catId) {
        log.info("DELETE, /admin/categories/{catId}, catId = {}", catId);
        categoryService.removeCategory(catId);
        log.info("deleting a category = {} successfully", catId);
    }
}
