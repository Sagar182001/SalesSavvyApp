package com.example.demo.dto.Admin;

import lombok.Data;

@Data
public class ProductSizeRequestDTO {

    private String size;   // S, M, L, XL
    private Integer stock; // optional
}


