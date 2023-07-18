package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(CategoryCreateDto categoryCreateDto) {
        return Category.builder()
                .name(categoryCreateDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }
}
