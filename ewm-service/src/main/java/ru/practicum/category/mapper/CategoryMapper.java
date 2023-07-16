package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

public class CategoryMapper {
    public static Category toCategory(CategoryCreateDto categoryCreateDto) {
        return new Category(categoryCreateDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }
}
