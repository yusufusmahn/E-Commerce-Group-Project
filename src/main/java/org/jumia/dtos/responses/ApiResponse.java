package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class ApiResponse {
    private Object data;
    private boolean success;

    public ApiResponse(Object data, boolean success) {
        this.data = data;
        this.success = success;
    }
}

