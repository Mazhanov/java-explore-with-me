package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.core.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryCreateDto categoryCreateDto) {
        Category category = CategoryMapper.toCategory(categoryCreateDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto changeCategory(int categoryId, CategoryDto categoryDto) {
        Category category = findCategoryByIdAndCheck(categoryId);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void removeCategory(int categoryId) {
        Category category = findCategoryByIdAndCheck(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(int id) {
        Category category = findCategoryByIdAndCheck(id);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category findCategoryByIdAndCheck(int id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Category", id));
    }
}
