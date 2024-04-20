package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.CategoryDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.ExistedDataException;
import com.simpleblogapi.simpleblogapi.models.Category;
import com.simpleblogapi.simpleblogapi.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapi.responses.ListCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ICategoryService {
   List<CategoryResponse>  findAll();
   CategoryResponse findById(Long id) throws DataNotFoundException;
   CategoryResponse createCategory(CategoryDTO categoryDTO) throws ExistedDataException;

    void deleteCategory(Long id) throws DataNotFoundException;

    CategoryResponse updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException, ExistedDataException;
}
