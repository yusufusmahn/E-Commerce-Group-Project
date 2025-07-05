package org.jumia.dtos.requests;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.jumia.data.models.OrderStatus;

import java.util.List;


@Data
public class UpdateOrderRequest {
    @Positive(message = "Total price must be greater than zero.")
    private Double totalPrice;

    private String orderStatus; // ✅ Changed from String

    private List<OrderedProductDTO> products; // ✅ updated type
}
