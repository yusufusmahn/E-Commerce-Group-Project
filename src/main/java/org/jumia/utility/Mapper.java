package org.jumia.utility;

import org.jumia.data.models.*;
import org.jumia.data.respositories.*;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.jumia.security.RoleValidator.getRoleNames;

public class Mapper {

//    public static User toEntity(RegisterUserRequest request) {
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword());
//        return user;
//    }

    public static User toEntity(RegisterUserRequest request) {
        User user = new User();


        // Clean and format inputs
        user.setEmail(cleanEmail(request.getEmail()));
        user.setName(formatFullName(request.getName()));
        user.setPassword(request.getPassword());
        return user;
    }


    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRoles(getRoleNames(user.getRoles()));
        response.setStoreName(user.getStoreName());
        response.setContactInfo(user.getContactInfo());
        response.setDescription(user.getDescription());
        return response;
    }

    public static Product mapCreateProductRequestToProduct(CreateProductRequest request, Category category) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantityAvailable(request.getQuantityAvailable());


        product.setCategoryId(category.getId());
        product.setCategoryName(category.getName());
        return product;
    }

    public static Product mapUpdateProductRequestToProduct(UpdateProductRequest request, Product existingProduct, Category category) {
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantityAvailable(request.getQuantityAvailable());

        if (category != null) {
            existingProduct.setCategoryId(category.getId());
            existingProduct.setCategoryName(category.getName());
        }
        return existingProduct;
    }

    public static ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantityAvailable(product.getQuantityAvailable());
        response.setSellerId(product.getSellerId());
        response.setImageUrl(product.getImageUrl());

        response.setCategoryId(product.getCategoryId());
        response.setCategoryName(product.getCategoryName());
        response.setStatus(product.getStatus().name());
        response.setRejectionReason(product.getRejectionReason());


        return response;
    }

    public static Order mapCreateOrderRequestToOrder(CreateOrderRequest request, List<Product> productList) {
        Order order = new Order();
//        order.setTotalPrice(request.getTotalPrice());

        if (request.getOrderStatus() != null) {
            try {
                order.setStatus(OrderStatus.valueOf(request.getOrderStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + request.getOrderStatus());
            }
        } else {
            order.setStatus(OrderStatus.PENDING);
        }

        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for (OrderedProductDTO dto : request.getProducts()) {
            Product product = findProductById(dto.getProductId(), productList);

            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setProduct(product);
            orderedProduct.setQuantity(dto.getQuantity());

            // Snapshot fields
            orderedProduct.setNameAtPurchase(product.getName());
            orderedProduct.setPriceAtPurchase(product.getPrice());
            orderedProduct.setImageUrlAtPurchase(product.getImageUrl());

            orderedProducts.add(orderedProduct);
        }

        order.setProducts(orderedProducts);
        order.setCreatedAt(LocalDateTime.now());
        order.setPaymentReference(request.getPaymentReference());
        return order;
    }

    public static Order mapUpdateOrderRequestToOrder(UpdateOrderRequest request, Order existingOrder, List<Product> productList) {
        if (request.getOrderStatus() != null) {
            try {
                existingOrder.setStatus(OrderStatus.valueOf(request.getOrderStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + request.getOrderStatus());
            }
        }

        if (request.getProducts() != null) {
            List<OrderedProduct> updatedProducts = new ArrayList<>();
            for (OrderedProductDTO dto : request.getProducts()) {
                Product product = findProductById(dto.getProductId(), productList);

                OrderedProduct op = new OrderedProduct();
                op.setProduct(product);
                op.setQuantity(dto.getQuantity());
                op.setNameAtPurchase(product.getName());
                op.setPriceAtPurchase(product.getPrice());
                op.setImageUrlAtPurchase(product.getImageUrl());

                updatedProducts.add(op);
            }
            existingOrder.setProducts(updatedProducts);
            existingOrder.setTotalPrice(calculateTotalPrice(updatedProducts));
        }

        return existingOrder;
    }


    public static List<OrderedProductResponse> buildOrderedProductResponses(List<OrderedProduct> orderedProducts) {
        List<OrderedProductResponse> responseList = new ArrayList<>();

        for (OrderedProduct op : orderedProducts) {
            OrderedProductResponse dto = new OrderedProductResponse();
            dto.setProductId(op.getProduct().getId());
            dto.setQuantity(op.getQuantity());
            dto.setName(op.getNameAtPurchase());
            dto.setPrice(op.getPriceAtPurchase());
            dto.setImageUrl(op.getImageUrlAtPurchase());
            responseList.add(dto);
        }

        return responseList;
    }

    public static OrderResponse mapOrderToOrderResponse(Order order, List<OrderedProductResponse> productDetails) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt().toString());
        response.setProducts(productDetails);
        return response;
    }

    public static List<OrderedProduct> mapToOrderedProductList(List<OrderedProductDTO> dtoList) {
        List<OrderedProduct> list = new ArrayList<>();
        for (OrderedProductDTO dto : dtoList) {
            OrderedProduct op = new OrderedProduct();
            Product p = new Product();
            p.setId(dto.getProductId());
            op.setProduct(p);
            op.setQuantity(dto.getQuantity());
            list.add(op);
        }
        return list;
    }

    public static CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        List<CartItemResponse> itemResponses = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setName(item.getProduct().getName());
            itemResponse.setDescription(item.getProduct().getDescription());
            itemResponse.setPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setTotalPrice(item.getTotalPrice());
            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);
        response.setTotalAmount(cart.getTotalAmount());
        return response;
    }

    public static List<ProductResponse> mapProductListToResponseList(List<Product> products) {
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : products) {
            responses.add(mapProductToProductResponse(product));
        }
        return responses;
    }

    public static List<OrderResponse> mapOrderListToResponseList(List<Order> orders) {
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderedProductResponse> productResponses = buildOrderedProductResponses(order.getProducts());
            responses.add(mapOrderToOrderResponse(order, productResponses));
        }
        return responses;
    }

    public static List<UserResponse> mapUserListToResponseList(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(toResponse(user));
        }
        return responses;
    }

    public static UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRoles(getRoleNames(user.getRoles()));
        return response;
    }

    private static Product findProductById(String productId, List<Product> products) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Product not found: " + productId);
    }


    public static CategoryResponse mapCategoryToResponse(Category category, long productCount) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setProductCount(productCount);
        return response;
    }


    public static List<CategoryResponse> mapCategoryListToResponseList(List<Category> categories, ProductRepository productRepository) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            long count = productRepository.countByCategoryId(category.getId());
            responses.add(mapCategoryToResponse(category, count));
        }
        return responses;
    }

    private static double calculateTotalPrice(List<OrderedProduct> products) {
        double total = 0.0;
        for (OrderedProduct op : products) {
            total += op.getPriceAtPurchase() * op.getQuantity();
        }
        return total;
    }

    public static Product mapCsvRowToProduct(
            String name,
            String description,
            double price,
            int quantity,
            Category category,
            String imageUrl,
            String sellerId
    ) {
        Product product = new Product();
        product.setName(Mapper.formatFullName(name));
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantityAvailable(quantity);
        product.setCategoryId(category.getId());
        product.setCategoryName(category.getName());
        product.setImageUrl(imageUrl);
        product.setStatus(ProductStatus.PENDING);
        product.setSellerId(sellerId);
        return product;
    }




    public static String cleanEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    public static String formatFullName(String input) {
        if (input == null || input.isBlank()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return formatted.toString().trim();
    }


    public static String formatSentenceCase(String input) {
        if (input == null || input.isBlank()) return input;
        input = input.trim().toLowerCase();
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }



}


/*
package org.jumia.utility;

import org.jumia.data.models.*;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.security.RoleValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jumia.security.RoleValidator.getRoleNames;

public class Mapper {

    public static User toEntity(RegisterUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRoles(getRoleNames(user.getRoles()));
        response.setStoreName(user.getStoreName());
        response.setContactInfo(user.getContactInfo());
        response.setDescription(user.getDescription());
        return response;
    }


    public static Product mapCreateProductRequestToProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantityAvailable(request.getQuantityAvailable());
        return product;
    }

    public static Product mapUpdateProductRequestToProduct(UpdateProductRequest request, Product existingProduct) {
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantityAvailable(request.getQuantityAvailable());
        return existingProduct;
    }

    public static ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantityAvailable(product.getQuantityAvailable());
        response.setSellerId(product.getSellerId());
        response.setImageUrl(product.getImageUrl());

        return response;
    }

    public static Order mapCreateOrderRequestToOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setTotalPrice(request.getTotalPrice());

        if (request.getOrderStatus() != null) {
            try {
                order.setStatus(OrderStatus.valueOf(request.getOrderStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + request.getOrderStatus());
            }
        } else {
            order.setStatus(OrderStatus.PENDING); // ✅ default
        }

        List<OrderedProduct> orderedProducts = new ArrayList<>();




    public static Order mapUpdateOrderRequestToOrder(UpdateOrderRequest request, Order existingOrder) {
        if (request.getTotalPrice() != null) {
            existingOrder.setTotalPrice(request.getTotalPrice());
        }

        if (request.getOrderStatus() != null) {
            try {
                existingOrder.setStatus(OrderStatus.valueOf(request.getOrderStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + request.getOrderStatus());
            }
        }

        if (request.getProducts() != null) {
            List<OrderedProduct> updatedProducts = new java.util.ArrayList<>();
            for (OrderedProductDTO dto : request.getProducts()) {
                OrderedProduct op = new OrderedProduct();
                Product p = new Product();
                p.setId(dto.getProductId()); // Only ID needed for DBRef
                op.setProduct(p);
                op.setQuantity(dto.getQuantity());
                updatedProducts.add(op);
            }
            existingOrder.setProducts(updatedProducts);
        }

        return existingOrder;
    }


    public static OrderResponse mapOrderToOrderResponse(Order order) {
        List<OrderedProductResponse> productDetails = new ArrayList<>();

        for (OrderedProduct orderedProduct : order.getProducts()) {
            OrderedProductResponse dto = new OrderedProductResponse();
            dto.setProductId(orderedProduct.getProduct().getId());
            dto.setQuantity(orderedProduct.getQuantity());
            dto.setName("Unknown Product"); // Placeholder when name not available
            productDetails.add(dto);
        }

        return mapOrderToOrderResponse(order, productDetails);
    }

    public static OrderResponse mapOrderToOrderResponse(Order order, List<OrderedProductResponse> productDetails) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt().toString());
        response.setProducts(productDetails);
        return response;
    }


    public static List<OrderedProduct> mapToOrderedProductList(List<OrderedProductDTO> dtoList) {
        List<OrderedProduct> list = new ArrayList<>();
        for (OrderedProductDTO dto : dtoList) {
            OrderedProduct op = new OrderedProduct();

            Product p = new Product();
            p.setId(dto.getProductId());
            op.setProduct(p);

            op.setQuantity(dto.getQuantity());

            list.add(op);
        }
        return list;
    }


    public static CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        List<CartItemResponse> itemResponses = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setName(item.getProduct().getName());
            itemResponse.setDescription(item.getProduct().getDescription());
            itemResponse.setPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setTotalPrice(item.getTotalPrice());
            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);
        response.setTotalAmount(cart.getTotalAmount());
        return response;
    }





    public static List<ProductResponse> mapProductListToResponseList(List<Product> products) {
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : products) {
            responses.add(mapProductToProductResponse(product));
        }
        return responses;
    }

    public static List<OrderResponse> mapOrderListToResponseList(List<Order> orders) {
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(mapOrderToOrderResponse(order)); // ✅ 1-arg version that builds minimal product info
        }
        return responses;
    }


    public static List<UserResponse> mapUserListToResponseList(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(toResponse(user));
        }
        return responses;
    }


    public static UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRoles(getRoleNames(user.getRoles()));



//        Set<String> roleNames = new HashSet<>();
//        for (Role role : user.getRoles()) {
//            roleNames.add(role.name());
//        }
//        response.setRoles(roleNames);

        return response;
    }



}


 */