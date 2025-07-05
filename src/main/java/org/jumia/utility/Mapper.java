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
        for (OrderedProductDTO dto : request.getProducts()) {
            OrderedProduct orderedProduct = new OrderedProduct();
            Product product = new Product();
            product.setId(dto.getProductId());
            orderedProduct.setProduct(product);
            orderedProduct.setQuantity(dto.getQuantity());
            orderedProducts.add(orderedProduct);
        }

        order.setProducts(orderedProducts);
        order.setCreatedAt(LocalDateTime.now()); // ✅ auto timestamp

        return order;
    }




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
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setProductIds(cart.getProductIds());
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

