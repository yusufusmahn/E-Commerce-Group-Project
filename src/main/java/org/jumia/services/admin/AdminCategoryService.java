package org.jumia.services.admin;

import org.jumia.dtos.requests.CreateCategoryRequest;
import org.jumia.dtos.responses.CategoryResponse;

import java.util.List;

public interface AdminCategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    List<CategoryResponse> getAllCategories();
}
