package org.jumia.controllers.customer;

import org.jumia.dtos.requests.ProductSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.jumia.dtos.responses.*;
import org.jumia.services.customer.CustomerProductService;

import java.util.List;

@RestController
@RequestMapping("/api/customer/products")
public class CustomerProductController {

    @Autowired
    private CustomerProductService customerProductService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductResponse> products = customerProductService.getAllProducts();
        return new ResponseEntity<>(new ApiResponse(products, true), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable String productId) {
        ProductResponse product = customerProductService.getProductById(productId);
        return new ResponseEntity<>(new ApiResponse(product, true), HttpStatus.OK);
    }

    @PostMapping("/search")
    public List<ProductResponse> searchProducts(@RequestBody ProductSearchRequest request) {
        return customerProductService.searchProducts(request);
    }

}
