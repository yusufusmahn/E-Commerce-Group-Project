////package org.jumia.controllers.seller;
////
////import jakarta.validation.Valid;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.core.Authentication;
////import org.springframework.web.bind.annotation.*;
////import org.jumia.dtos.requests.CreateProductRequest;
////import org.jumia.dtos.requests.UpdateProductRequest;
////import org.jumia.dtos.requests.UpdateStockRequest;
////import org.jumia.dtos.responses.ApiResponse;
////import org.jumia.dtos.responses.ProductResponse;
////import org.jumia.services.seller.SellerProductService;
////import org.springframework.web.multipart.MultipartFile;
////
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/seller/products")
////public class SellerProductController {
////
////    @Autowired
////    private SellerProductService SellerProductService;
////
////
////    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
////    public ResponseEntity<ApiResponse> addProduct(
////            @ModelAttribute @Valid CreateProductRequest request,
////            @RequestPart("image") MultipartFile image,
////            Authentication authentication) {
////        String sellerId = authentication.getName();
////        ProductResponse response = SellerProductService.addProduct(request, image, sellerId);
////        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
////    }
////
////
////    @PutMapping(value = "/update/{productId}", consumes = {"multipart/form-data"})
////    public ResponseEntity<ApiResponse> updateProduct(
////            @PathVariable String productId,
////            @ModelAttribute @Valid UpdateProductRequest request,
////            @RequestPart(value = "image", required = false) MultipartFile image,
////            Authentication authentication) {
////        String sellerId = authentication.getName();
////        ProductResponse response = SellerProductService.updateProduct(productId, request, image, sellerId);
////        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
////    }
////
////
////    @DeleteMapping("/{productId}")
////    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId, Authentication authentication) {
////        String sellerId = authentication.getName();
////        SellerProductService.deleteProduct(productId, sellerId);
////        return new ResponseEntity<>(new ApiResponse("Product deleted successfully", true), HttpStatus.OK);
////    }
////
////
////    @GetMapping("/{productId}")
////    public ResponseEntity<ApiResponse> getProductById(@PathVariable String productId, Authentication authentication) {
////        String sellerId = authentication.getName();
////        ProductResponse response = SellerProductService.getProductById(productId, sellerId);
////        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
////    }
////
////
////    @PutMapping("/products/stock")
////    public ResponseEntity<ApiResponse> updateStock(
////            @Valid @RequestBody UpdateStockRequest request,
////            Authentication authentication) {
////        String sellerId = authentication.getName();
////        ProductResponse response = SellerProductService.updateStock(request, sellerId);
////        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
////    }
////
////    @GetMapping
////    public ResponseEntity<ApiResponse> getAllProducts() {
////        List<ProductResponse> products = SellerProductService.getAllProducts();
////        return new ResponseEntity<>(new ApiResponse(products, true), HttpStatus.OK);
////    }
////
////    @GetMapping("/seller")
////    public ResponseEntity<ApiResponse> getProductsBySeller(Authentication authentication) {
////        String sellerId = authentication.getName();
////        List<ProductResponse> products = SellerProductService.getProductsBySellerId(sellerId);
////        return new ResponseEntity<>(new ApiResponse(products, true), HttpStatus.OK);
////    }
//
//package org.jumia.controllers.seller;
//
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import org.jumia.dtos.requests.CreateProductRequest;
//import org.jumia.dtos.requests.UpdateProductRequest;
//import org.jumia.dtos.requests.UpdateStockRequest;
//import org.jumia.dtos.responses.ApiResponse;
//import org.jumia.dtos.responses.ProductResponse;
//import org.jumia.services.seller.SellerProductService;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/seller/products")
//public class SellerProductController {
//
//    @Autowired
//    private SellerProductService sellerProductService;
//
//    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
//    public ResponseEntity<ApiResponse> addProduct(
//            @ModelAttribute @Valid CreateProductRequest request,
//            @RequestPart("image") MultipartFile image,
//            Authentication authentication) {
//        String sellerId = authentication.getName();
//        ProductResponse response = sellerProductService.addProduct(request, image, sellerId);
//        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
//    }
//
//    @PutMapping(value = "/update/{productId}", consumes = {"multipart/form-data"})
//    public ResponseEntity<ApiResponse> updateProduct(
//            @PathVariable String productId,
//            @ModelAttribute @Valid UpdateProductRequest request,
//            @RequestPart(value = "image", required = false) MultipartFile image,
//            Authentication authentication) {
//        String sellerId = authentication.getName();
//        ProductResponse response = sellerProductService.updateProduct(productId, request, image, sellerId);
//        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{productId}")
//    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId, Authentication authentication) {
//        String sellerId = authentication.getName();
//        sellerProductService.deleteProduct(productId, sellerId);
//        return new ResponseEntity<>(new ApiResponse("Product deleted successfully", true), HttpStatus.OK);
//    }
//
//    @GetMapping("/getProduct/{productId}")
//    public ResponseEntity<ApiResponse> getProductById(@PathVariable String productId, Authentication authentication) {
//        String sellerId = authentication.getName();
//        ProductResponse response = sellerProductService.getProductById(productId, sellerId);
//        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
//    }
//
//    @PutMapping("/products/stock")
//    public ResponseEntity<ApiResponse> updateStock(
//            @Valid @RequestBody UpdateStockRequest request,
//            Authentication authentication) {
//        String sellerId = authentication.getName();
//        ProductResponse response = sellerProductService.updateStock(request, sellerId);
//        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
//    }
//
//    @GetMapping("/allProducts")
//    public ResponseEntity<ApiResponse> getAllProducts() {
//        List<ProductResponse> products = sellerProductService.getAllProducts();
//        return new ResponseEntity<>(new ApiResponse(products, true), HttpStatus.OK);
//    }
//
//    @GetMapping("/seller")
//    public ResponseEntity<ApiResponse> getProductsBySeller(Authentication authentication) {
//        String sellerId = authentication.getName();
//        List<ProductResponse> products = sellerProductService.getProductsBySellerId(sellerId);
//        return new ResponseEntity<>(new ApiResponse(products, true), HttpStatus.OK);
//    }
//}
//
//
//
//
//
package org.jumia.controllers.seller;

import jakarta.validation.Valid;
import org.jumia.dtos.requests.CreateProductRequest;
import org.jumia.dtos.requests.UpdateProductRequest;
import org.jumia.dtos.requests.UpdateStockRequest;
import org.jumia.dtos.responses.ProductResponse;
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
}
