package org.jumia.services.seller;

import org.jumia.dtos.responses.CategoryResponse;
import java.util.List;

public interface SellerCategoryService {
    List<CategoryResponse> getAllCategories();

}
