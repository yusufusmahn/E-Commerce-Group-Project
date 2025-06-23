package org.temu.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private Double totalPrice;
    private String orderStatus;
    private List<String> products;
    private String createdAt;

}
