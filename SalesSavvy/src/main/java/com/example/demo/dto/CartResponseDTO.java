package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDTO {

	    private Integer productId;
	    private String productName;
	    private Integer quantity;
	    private BigDecimal price;
	    private Integer stock;
	    private String categoryName;
	    private List<String> images;
	    
	 // 🔥 ADD THIS
	    private String size;

	    // getters & setters
	    public String getSize() {
	        return size;
	    }

	    public void setSize(String size) {
	        this.size = size;
	    }
	    
	   // getters and setters
	    
		public Integer getProductId() {
			return productId;
		}
		public void setProductId(Integer productId) {
			this.productId = productId;
		}
		
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
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
		  
}

