package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.core.pagination.PaginationMapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@AllArgsConstructor
@Slf4j
@Valid
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET, /categories");
        List<CategoryDto> categoryDtoList = categoryService.getCategories(PaginationMapper.toPageable(from, size));
        log.info("get a list of categories = {}", categoryDtoList);
        return categoryDtoList;
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable int catId) {
        log.info("GET, /categories/{catId}, catId = {}", catId);
        CategoryDto categoryDto = categoryService.getCategoryById(catId);
        log.info("get a category = {}", categoryDto);
        return categoryDto;
    }
}
