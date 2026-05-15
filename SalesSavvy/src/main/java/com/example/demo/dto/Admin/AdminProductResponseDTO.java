package com.example.demo.dto.Admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dto.ProductSizeResponseDTO;

public class AdminProductResponseDTO {

    private Integer productId;
    
    private String name;
    
    private String description;
    
    private BigDecimal price;
    
    private Integer stock;
    
    private Integer categoryId;
    
    private String categoryName;
    
    private List<String> images;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<ProductSizeResponseDTO> sizes;
    
    
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<String> getImages() {
		return images;
	}
	
	public void setImages(List<String> images) {
		this.images = images;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<ProductSizeResponseDTO> getSizes() {
		return sizes;
	}
	public void setSizes(List<ProductSizeResponseDTO> sizes) {
		this.sizes = sizes;
	}

    
}
