package org.jumia.controllers.Admin;

import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.services.admin.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private AdminProductService adminProductService;


    @PostMapping(value = "/addProduct", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductResponse> addProduct(
            @ModelAttribute @Valid CreateProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ProductResponse response = adminProductService.addProductAsAdmin(request, image);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @ModelAttribute @Valid UpdateProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ProductResponse updated = adminProductService.updateProductAsAdmin(id, request, image);
        return ResponseEntity.ok(updated);
    }


    // 🔐 SUPER_ADMIN only
//    @PostMapping
//    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody CreateProductRequest request) {
//        ProductResponse response = adminProductService.addProductAsAdmin(request);
//        return ResponseEntity.ok(response);
//    }
//
//    // 🔐 ADMIN or SUPER_ADMIN
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id, @Valid @RequestBody UpdateProductRequest request) {
//        ProductResponse updated = adminProductService.updateProductAsAdmin(id, request);
//        return ResponseEntity.ok(updated);
//    }

    // 🔐 SUPER_ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        adminProductService.deleteProductAsAdmin(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    // 🔐 ADMIN or SUPER_ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        ProductResponse response = adminProductService.getProductByIdAsAdmin(id);
        return ResponseEntity.ok(response);
    }

    // 🔐 ADMIN or SUPER_ADMIN
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = adminProductService.getAllProductsAsAdmin();
        return ResponseEntity.ok(products);
    }
}
