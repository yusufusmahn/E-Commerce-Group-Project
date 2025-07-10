package org.jumia.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;



@Data
public class CreateOrderRequest {
//    @NotNull(message = "Total price is required.")
//    @Positive(message = "Total price must be greater than zero.")
//    private Double totalPrice;

    private String orderStatus;

    @NotEmpty(message = "Products list must not be empty.")
    private List<OrderedProductDTO> products;

    private String paymentReference;

}




