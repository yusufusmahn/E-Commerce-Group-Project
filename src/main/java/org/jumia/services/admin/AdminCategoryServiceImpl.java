package org.jumia.services.admin;

import org.jumia.data.models.Category;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CategoryRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.CreateCategoryRequest;
import org.jumia.dtos.responses.CategoryResponse;
import org.jumia.exceptions.ResourceAlreadyExistsException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(user);

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists with name: " + request.getName());
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
        return Mapper.mapCategoryListToResponseList(categories, productRepository);
    }
}
