package org.jumia.controllers.ShopProduct;

import org.jumia.dtos.responses.ProductResponse;
import org.jumia.services.ShopProduct.ShopProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/products")
public class ShopProductController {

    @Autowired
    private ShopProductService shopProductService;



//    @GetMapping
//    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam(required = false) String categoryId) {
//        List<ProductResponse> products = shopProductService.getProducts(categoryId);
//        return ResponseEntity.ok(products);
//    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        ProductResponse response = shopProductService.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@RequestParam String categoryId) {
        List<ProductResponse> products = shopProductService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts(@RequestParam(required = false) String categoryId) {
        return shopProductService.getAllProducts(categoryId);
    }


    @GetMapping("/by-category-name")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryName(@RequestParam String name) {
        List<ProductResponse> products = shopProductService.getProductsByCategoryName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@RequestParam String name) {
        List<ProductResponse> products = shopProductService.getProductsByName(name);
        return ResponseEntity.ok(products);
    }



}
