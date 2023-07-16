package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryCreateDto categoryCreateDto);

    CategoryDto changeCategory(int categoryId, CategoryDto categoryDto);

    void removeCategory(int categoryId);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(int id);
}
