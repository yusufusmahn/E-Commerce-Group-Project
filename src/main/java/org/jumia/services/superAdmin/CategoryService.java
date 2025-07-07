package org.jumia.services.superAdmin;

import org.jumia.dtos.requests.CreateCategoryRequest;
import org.jumia.dtos.requests.UpdateCategoryRequest;
import org.jumia.dtos.responses.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(String id, UpdateCategoryRequest request);
    void deleteCategory(String id);

}
