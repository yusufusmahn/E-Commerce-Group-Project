
package org.jumia.controllers.seller;

import jakarta.validation.Valid;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.services.seller.SellerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/seller/products")
public class SellerProductController {

    @Autowired
    private SellerProductService sellerProductService;

        @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductResponse> addProduct(
                @ModelAttribute @Valid CreateProductRequest request,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.ok(sellerProductService.addProduct(request, image));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String productId,
            @RequestPart("data") UpdateProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(sellerProductService.updateProduct(productId, request, image));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        sellerProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getSellerProducts() {
        return ResponseEntity.ok(sellerProductService.getProductsBySellerId());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(sellerProductService.getProductById(productId));
    }

    @PatchMapping("/stock")
    public ResponseEntity<ProductResponse> updateStock(@RequestBody UpdateStockRequest request) {
        return ResponseEntity.ok(sellerProductService.updateStock(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(sellerProductService.getAllProducts());
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@RequestParam String categoryId) {
        return ResponseEntity.ok(sellerProductService.getProductsByCategory(categoryId));
    }

    @GetMapping("/my-products")
    public List<ProductResponse> getMyProducts(@RequestParam(required = false) String categoryIdOrName) {
        return sellerProductService.getMyProducts(categoryIdOrName);
    }



    @PostMapping(value = "/upload-csv", consumes = {"multipart/form-data"})
    public ResponseEntity<List<ProductResponse>> uploadCSVWithImages(
            @RequestPart("file") MultipartFile csvFile,
            @RequestPart("images") List<MultipartFile> images
    ) {
        return ResponseEntity.ok(sellerProductService.uploadCSV(csvFile, images));
    }

}
