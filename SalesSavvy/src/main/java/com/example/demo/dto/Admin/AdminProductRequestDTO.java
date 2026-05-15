package com.example.demo.dto.Admin;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class AdminProductRequestDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer categoryId;
    private Boolean active;
    
    private Integer stock;
    
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }


    // 🔥 STEP 5 addition
    private List<ProductSizeRequestDTO> sizes;
    
    
    
}
