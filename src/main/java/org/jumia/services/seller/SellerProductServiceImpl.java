package org.jumia.services.seller;

import org.jumia.security.CurrentUserProvider;
import org.jumia.services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.*;
import org.jumia.data.respositories.*;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.exceptions.*;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class SellerProductServiceImpl implements SellerProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public ProductResponse addProduct(CreateProductRequest request, MultipartFile image) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(seller, Role.SELLER);

        final long MAX_FILE_SIZE = 2 * 1024 * 1024;
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the limit of 2MB");
        }

        String imageUrl;
        try {
            imageUrl = cloudinaryService.uploadImage(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        Product product = Mapper.mapCreateProductRequestToProduct(request, category);
        product.setImageUrl(imageUrl);
        product.setSellerId(seller.getId());

        product.setStatus(ProductStatus.PENDING);
        product.setRejectionReason(null);


        Product savedProduct = productRepository.save(product);
        return Mapper.mapProductToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(String productId, UpdateProductRequest request, MultipartFile image) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(seller, Role.SELLER);

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!existingProduct.getSellerId().equals(seller.getId())) {
            throw new AccessDeniedException("You can only update your own products.");
        }

        String imageUrl = existingProduct.getImageUrl();
        if (image != null && !image.isEmpty()) {
            final long MAX_FILE_SIZE = 2 * 1024 * 1024;
            if (image.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds the limit of 2MB");
            }

            try {
                imageUrl = cloudinaryService.uploadImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));
        }


        Product updatedProduct = Mapper.mapUpdateProductRequestToProduct(request, existingProduct, category);
        updatedProduct.setImageUrl(imageUrl);

        existingProduct.setStatus(ProductStatus.PENDING);
        existingProduct.setRejectionReason(null);


        Product savedProduct = productRepository.save(updatedProduct);
        return Mapper.mapProductToProductResponse(savedProduct);
    }

    @Override
    public void deleteProduct(String productId) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(seller, Role.SELLER);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!product.getSellerId().equals(seller.getId())) {
            throw new AccessDeniedException("You can only delete your own products.");
        }

        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getProductsBySellerId() {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(seller, Role.SELLER);

        List<Product> products = productRepository.findBySellerId(seller.getId());
        return Mapper.mapProductListToResponseList(products);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(seller, Role.SELLER);

        Product product = productRepository.findByIdAndSellerId(productId, seller.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found or does not belong to seller."));
        return Mapper.mapProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
//        List<Product> products = productRepository.findByStatus(ProductStatus.APPROVED);
        List<Product> products = productRepository.findAll();

        return Mapper.mapProductListToResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categoryId) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Product> products = productRepository.findBySellerIdAndCategoryId(seller.getId(), categoryId);
        return Mapper.mapProductListToResponseList(products);
    }


    @Override
    public ProductResponse updateStock(UpdateStockRequest request) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(seller, Role.SELLER);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        if (!product.getSellerId().equals(seller.getId())) {
            throw new AccessDeniedException("You can only update stock for your own products.");
        }

        product.setQuantityAvailable(request.getNewStock());
        Product updatedProduct = productRepository.save(product);
        return Mapper.mapProductToProductResponse(updatedProduct);
    }

    @Override
    public List<ProductResponse> getMyProducts(String categoryIdOrName) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Product> products;

        if (categoryIdOrName != null && !categoryIdOrName.isBlank()) {
            if (categoryRepository.existsById(categoryIdOrName)) {
                products = productRepository.findBySellerIdAndCategoryId(seller.getId(), categoryIdOrName);
            } else {
                Category category = categoryRepository.findByNameIgnoreCase(categoryIdOrName)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryIdOrName));
                products = productRepository.findBySellerIdAndCategoryId(seller.getId(), category.getId());
            }
        } else {
            products = productRepository.findBySellerId(seller.getId());
        }

        return Mapper.mapProductListToResponseList(products);
    }

    @Override
    public List<ProductResponse> uploadCSV(MultipartFile csvFile, List<MultipartFile> images) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Product> savedProducts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip headers
                }

                String[] fields = line.split(",");

                if (fields.length < 6) {
                    throw new IllegalArgumentException("Invalid CSV format. Expecting: name,description,price,quantity,categoryId,imageName");
                }

                String name = fields[0].trim();
                String description = fields[1].trim();
                double price;
                int quantity;

                try {
                    price = Double.parseDouble(fields[2].trim());
                    quantity = Integer.parseInt(fields[3].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format in CSV: " + e.getMessage());
                }

                String categoryId = fields[4].trim();
                String imageName = fields[5].trim().toLowerCase(); // normalize filename

                // Find matching image with normalized name
                MultipartFile imageFile = null;
                for (MultipartFile img : images) {
                    String originalName = img.getOriginalFilename();
                    if (originalName != null && originalName.toLowerCase().trim().equals(imageName)) {
                        imageFile = img;
                        break;
                    }
                }

                if (imageFile == null) {
                    throw new ResourceNotFoundException("Image not found: " + imageName);
                }

                if (imageFile.getSize() > 2 * 1024 * 1024) {
                    throw new IllegalArgumentException("Image " + imageName + " exceeds 2MB size limit");
                }

                String imageUrl = cloudinaryService.uploadImage(imageFile);

                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

                Product product = Mapper.mapCsvRowToProduct(
                        name,
                        description,
                        price,
                        quantity,
                        category,
                        imageUrl,
                        seller.getId()
                );

                savedProducts.add(product);
            }

            List<Product> saved = productRepository.saveAll(savedProducts);
            return Mapper.mapProductListToResponseList(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV or image", e);
        }
    }


    /*

    @Override
    public List<ProductResponse> uploadCSV(MultipartFile csvFile, List<MultipartFile> images) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Product> savedProducts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip headers
                }

                String[] fields = line.split(",");

                if (fields.length < 6) {
                    throw new IllegalArgumentException("Invalid CSV format. Expecting: name,description,price,quantity,categoryId,imageName");
                }

                String name = fields[0].trim();
                String description = fields[1].trim();
                double price = Double.parseDouble(fields[2].trim());
                int quantity = Integer.parseInt(fields[3].trim());
                String categoryId = fields[4].trim();
                String imageName = fields[5].trim();

                // Find matching image
//                MultipartFile imageFile = images.stream()
//                        .filter(img -> img.getOriginalFilename() != null && img.getOriginalFilename().equals(imageName))
//                        .findFirst()
//                        .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + imageName));

                // simplified
                MultipartFile imageFile = null;
                for (MultipartFile img : images) {
                    if (img.getOriginalFilename() != null && img.getOriginalFilename().equals(imageName)) {
                        imageFile = img;
                        break;
                    }
                }
                if (imageFile == null) {
                    throw new ResourceNotFoundException("Image not found: " + imageName);
                }



                if (imageFile.getSize() > 2 * 1024 * 1024) {
                    throw new IllegalArgumentException("Image " + imageName + " exceeds 2MB size limit");
                }

                String imageUrl = cloudinaryService.uploadImage(imageFile);

                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

//                Product product = new Product();
//                product.setName(name);
//                product.setDescription(description);
//                product.setPrice(price);
//                product.setQuantityAvailable(quantity);
//                product.setCategoryId(category.getId());
//                product.setCategoryName(category.getName());
//                product.setImageUrl(imageUrl);
//                product.setStatus(ProductStatus.PENDING);
//                product.setSellerId(seller.getId());

                Product product = Mapper.mapCsvRowToProduct(
                        name,
                        description,
                        price,
                        quantity,
                        category,
                        imageUrl,
                        seller.getId()
                );

                savedProducts.add(product);
            }

            List<Product> saved = productRepository.saveAll(savedProducts);
            return Mapper.mapProductListToResponseList(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV or image", e);
        }
    }

     */




}


//@Service
//public class SellerProductServiceImpl implements SellerProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CloudinaryService cloudinaryService;
//
//    @Override
//    public ProductResponse addProduct(CreateProductRequest request, MultipartFile image, String sellerId) {
////        String normalizedEmail = sellerId.toLowerCase();
//
//        User seller = userRepository.findByEmail(sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
//        System.out.println("Seller ID not found: " + sellerId);
//
//
//        if (RoleValidator.isNotSeller(seller)) {
//            System.out.println("Roles: " + seller.getRoles());
//            throw new AccessDeniedException("User is not authorized to add products.");
//        }
//
//
//        if (RoleValidator.isNotSeller(seller)) {
//            throw new AccessDeniedException("User is not authorized to add products.");
//        }
//
//        final long MAX_FILE_SIZE = 2 * 1024 * 1024;
//        if (image.getSize() > MAX_FILE_SIZE) {
//            throw new IllegalArgumentException("File size exceeds the limit of 2MB");
//        }
//
//        String imageUrl;
//        try {
//            imageUrl = cloudinaryService.uploadImage(image);
//        } catch (JumiaException e) {
//            throw new ImageUploadException("Failed to upload image to Cloudinary: ");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Product product = Mapper.mapCreateProductRequestToProduct(request);
//        product.setImageUrl(imageUrl);
//        product.setSellerId(sellerId);
//
//        Product savedProduct = productRepository.save(product);
//        return Mapper.mapProductToProductResponse(savedProduct);
//    }
//
//    @Override
//    public ProductResponse updateProduct(String productId, UpdateProductRequest request, MultipartFile image, String sellerId) {
//        User seller = userRepository.findByEmail(sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
//
//        if (RoleValidator.isNotSeller(seller)) {
//            throw new AccessDeniedException("User is not authorized to update products.");
//        }
//
//        Product existingProduct = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
//
//        if (!existingProduct.getSellerId().equals(sellerId)) {
//            throw new AccessDeniedException("You can only update your own products.");
//        }
//
//        String imageUrl = existingProduct.getImageUrl();
//        if (image != null && !image.isEmpty()) {
//            final long MAX_FILE_SIZE = 2 * 1024 * 1024;
//            if (image.getSize() > MAX_FILE_SIZE) {
//                throw new IllegalArgumentException("File size exceeds the limit of 2MB");
//            }
//
//            try {
//                imageUrl = cloudinaryService.uploadImage(image);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to upload image", e);
//            }
//        }
//
//        Product updatedProduct = Mapper.mapUpdateProductRequestToProduct(request, existingProduct);
//        updatedProduct.setImageUrl(imageUrl);
//
//        Product savedProduct = productRepository.save(updatedProduct);
//        return Mapper.mapProductToProductResponse(savedProduct);
//    }
//
//    @Override
//    public void deleteProduct(String productId, String sellerId) {
//        User seller = userRepository.findByEmail(sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
//
//        if (RoleValidator.isNotSeller(seller)) {
//            throw new AccessDeniedException("User is not authorized to delete products.");
//        }
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
//
//        if (!product.getSellerId().equals(sellerId)) {
//            throw new AccessDeniedException("You can only delete your own products.");
//        }
//
//        productRepository.delete(product);
//    }
//
//    @Override
//    public List<ProductResponse> getProductsBySellerId(String sellerId) {
//        User seller = userRepository.findByEmail(sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
//
//        if (RoleValidator.isNotSeller(seller)) {
//            throw new AccessDeniedException("User is not authorized to view products.");
//        }
//
//        List<Product> products = productRepository.findBySellerId(sellerId);
//        return Mapper.mapProductListToResponseList(products);
//    }
//
//    @Override
//    public ProductResponse getProductById(String productId, String sellerId) {
//        Product product = productRepository.findByIdAndSellerId(productId, sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found or does not belong to seller."));
//        return Mapper.mapProductToProductResponse(product);
//    }
//
//
//    @Override
//    public List<ProductResponse> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        return Mapper.mapProductListToResponseList(products);
//    }
//
//
//    @Override
//    public ProductResponse updateStock(UpdateStockRequest request, String sellerId) {
//        User seller = userRepository.findByEmail(sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
//
//        if (RoleValidator.isNotSeller(seller)) {
//            throw new AccessDeniedException("User is not authorized to update stock.");
//        }
//
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));
//
//        if (!product.getSellerId().equals(sellerId)) {
//            throw new AccessDeniedException("You can only update stock for your own products.");
//        }
//
//        product.setQuantityAvailable(request.getNewStock());
//        Product updatedProduct = productRepository.save(product);
//        return Mapper.mapProductToProductResponse(updatedProduct);
//    }



