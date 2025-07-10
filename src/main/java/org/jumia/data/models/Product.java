package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private Double price;
    private Integer quantityAvailable;
    private String sellerId;
    private String imageUrl;

    private String categoryId;
    private String categoryName;

    private ProductStatus status = ProductStatus.PENDING;
    private String rejectionReason;

}
