package org.jumia.services.superAdmin;

import org.jumia.data.models.Category;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CategoryRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.CreateCategoryRequest;
import org.jumia.dtos.requests.UpdateCategoryRequest;
import org.jumia.dtos.responses.CategoryResponse;
import org.jumia.exceptions.DuplicateResourceException;
import org.jumia.exceptions.EntityNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(user);

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category saved = categoryRepository.save(category);
        long count = productRepository.countByCategoryId(saved.getId());
        return Mapper.mapCategoryToResponse(saved, count);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return Mapper.mapCategoryListToResponseList(categories, productRepository); // ✅ include counts
    }



    @Override
    public CategoryResponse updateCategory(String id, UpdateCategoryRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(user);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);
        long count = productRepository.countByCategoryId(updated.getId());
        return Mapper.mapCategoryToResponse(updated, count);
    }

    @Override
    public void deleteCategory(String id) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(user);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        categoryRepository.delete(category);
    }


}
