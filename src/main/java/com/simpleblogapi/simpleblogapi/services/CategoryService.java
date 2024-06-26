package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.CategoryDTO;
import com.simpleblogapi.simpleblogapi.dtos.UpdateCategoryDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.ExistedDataException;
import com.simpleblogapi.simpleblogapi.models.Category;
import com.simpleblogapi.simpleblogapi.repositories.CategoryRepository;
import com.simpleblogapi.simpleblogapi.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapi.utils.CheckCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findById(Long id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        return CategoryResponse.fromCategory(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryDTO categoryDTO) throws ExistedDataException {
        if(categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new ExistedDataException("Category already exists");
        }
        Category newCategory = Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .build();
        return CategoryResponse.fromCategory(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteCategory(Long id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO) throws DataNotFoundException, ExistedDataException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        if(categoryRepository.existsByCategoryName(updateCategoryDTO.getCategoryName())) {
            throw new ExistedDataException("Category already exists");
        }
        BeanUtils.copyProperties(updateCategoryDTO, category, CheckCondition.getNullPropertyNames(updateCategoryDTO));
        return CategoryResponse.fromCategory(categoryRepository.save(category));
    }
}
