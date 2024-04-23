package com.simpleblogapi.simpleblogapi.controllers;

import com.simpleblogapi.simpleblogapi.dtos.CategoryDTO;
import com.simpleblogapi.simpleblogapi.dtos.UpdateCategoryDTO;
import com.simpleblogapi.simpleblogapi.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapi.responses.ListCategoryResponse;
import com.simpleblogapi.simpleblogapi.services.ICategoryService;
import com.simpleblogapi.simpleblogapi.utils.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ListCategoryResponse> getAllCategories() {
        List<CategoryResponse> categories = categoryService.findAll();
        ListCategoryResponse response = new ListCategoryResponse(categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            CategoryResponse category = categoryService.findById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {

        try {
            if(result.hasErrors()) {
                return ResponseEntity.badRequest().body(Validator.getMessageValidator(result));
            }
            CategoryResponse category = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                return ResponseEntity.badRequest().body(Validator.getMessageValidator(result));
            }
            CategoryResponse category = categoryService.updateCategory(id, updateCategoryDTO);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }

    }
}
