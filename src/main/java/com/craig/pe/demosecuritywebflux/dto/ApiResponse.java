package com.craig.pe.demosecuritywebflux.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private int status;
    private String message;
    private Object result;

    public ApiResponse(int status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
}
