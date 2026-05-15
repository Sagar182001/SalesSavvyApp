package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import com.example.demo.dto.ProductSizeResponseDTO;

public class ProductResponseDTO {


    private Integer productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private List<String> images;
    private String categoryName;
    private List<ProductSizeResponseDTO> sizes;
    
      
 // getters and setters
    
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
	
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public List<ProductSizeResponseDTO> getSizes() {
	    return sizes;
	}

	public void setSizes(List<ProductSizeResponseDTO> sizes) {
	    this.sizes = sizes;
	}

    
}