package org.jumia.dtos.requests;

import lombok.Data;
import org.jumia.data.models.OrderStatus;

import jakarta.validation.constraints.NotNull;

@Data
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}
