package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.CategoryDTO;
import com.simpleblogapi.simpleblogapi.dtos.UpdateCategoryDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.ExistedDataException;
import com.simpleblogapi.simpleblogapi.responses.CategoryResponse;

import java.util.List;

public interface ICategoryService {
   List<CategoryResponse>  findAll();
   CategoryResponse findById(Long id) throws DataNotFoundException;
   CategoryResponse createCategory(CategoryDTO categoryDTO) throws ExistedDataException;

    void deleteCategory(Long id) throws DataNotFoundException;

    CategoryResponse updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO) throws DataNotFoundException, ExistedDataException;
}
