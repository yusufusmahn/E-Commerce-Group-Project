package org.jumia.controllers.seller;

import org.jumia.dtos.responses.CategoryResponse;
import org.jumia.dtos.responses.ProductResponse;
import org.jumia.services.seller.SellerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/categories")
public class SellerCategoryController {

    @Autowired
    private SellerCategoryService sellerCategoryService;

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return sellerCategoryService.getAllCategories();
    }





}
