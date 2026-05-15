package com.example.demo.dto;


public class ProductSizeResponseDTO {

    private String size;
    private Integer stock;

    public ProductSizeResponseDTO() {}

    public ProductSizeResponseDTO(String size, Integer stock) {
        this.size = size;
        this.stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}


