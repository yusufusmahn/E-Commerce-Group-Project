package org.jumia.utility;

import org.jumia.data.models.*;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<String> roleNames = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleNames.add(role.name());
        }
        response.setRoles(roleNames);

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
        response.setQuantityAvailable(product.getQuantityAvailable()); // No errors now
        response.setSellerId(product.getSellerId());
        response.setImageUrl(product.getImageUrl()); // Map the new image URL field

        return response;
    }


    public static Order mapCreateOrderRequestToOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setId(request.getId());
        order.setUserId(request.getUserId());
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus(request.getOrderStatus());
        order.setProducts(request.getProducts());
        order.setCreatedAt(LocalDateTime.parse(request.getCreatedAt()));
        return order;
    }

    public static Order mapUpdateOrderRequestToOrder(UpdateOrderRequest request, Order existingOrder) {
        if (request.getTotalPrice() != null) {
            existingOrder.setTotalPrice(request.getTotalPrice());
        }
        if (request.getOrderStatus() != null) {
            existingOrder.setStatus(request.getOrderStatus());
        }
        if (request.getProducts() != null) {
            existingOrder.setProducts(request.getProducts());
        }
        return existingOrder;
    }

    public static OrderResponse mapOrderToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderStatus(order.getStatus());
        response.setProducts(order.getProducts());
        response.setCreatedAt(order.getCreatedAt().toString()); // Convert LocalDateTime to String
        return response;
    }

    public static CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setProductIds(cart.getProductIds());
        return response;
    }

    public static Product mapAdminProductRequestToProduct(AdminProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        return product;
    }

    public static Product mapAdminProductRequestToExistingProduct(AdminProductRequest request, Product existingProduct) {
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        return existingProduct;
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
            responses.add(mapOrderToOrderResponse(order));
        }
        return responses;
    }
    public static List<UserResponse> mapUserListToResponseList(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(toResponse(user)); // Reuse existing `toResponse` method
        }
        return responses;
    }

//    public static ProductResponse mapProductToProductResponse(Product product) {
//        ProductResponse response = new ProductResponse();
//        response.setId(product.getId());
//        response.setName(product.getName());
//        response.setDescription(product.getDescription());
//        response.setPrice(product.getPrice());
//        response.setQuantityAvailable(product.getQuantityAvailable());
//        response.setSellerId(product.getSellerId());
//        return response;
//    }

    public static UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        Set<String> roleNames = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleNames.add(role.name());
        }
        response.setRoles(roleNames);

        return response;
    }



}

