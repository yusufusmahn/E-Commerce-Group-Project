package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class CategoryResponse {
    private String id;
    private String name;
    private String description;
    private long productCount;

}
