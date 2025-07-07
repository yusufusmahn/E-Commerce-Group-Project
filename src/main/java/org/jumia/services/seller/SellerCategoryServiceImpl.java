package org.jumia.services.seller;

import org.jumia.data.models.Category;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CategoryRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.responses.CategoryResponse;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerCategoryServiceImpl implements SellerCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public List<CategoryResponse> getAllCategories() {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(user); // ensures only sellers can call this

        List<Category> categories = categoryRepository.findAll();
        return Mapper.mapCategoryListToResponseList(categories, productRepository);
    }
}
