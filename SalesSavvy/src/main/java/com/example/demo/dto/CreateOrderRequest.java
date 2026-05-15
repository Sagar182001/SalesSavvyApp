package com.example.demo.dto;

import java.util.List;


public class CreateOrderRequest {
	
	    private double totalAmount;
	    
	    private List<CartResponseDTO> cartitems;
	   
	    public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public List<CartResponseDTO> getCartitems() {
			return cartitems;
		}

		public void setCartitems(List<CartResponseDTO> cartitems) {
			this.cartitems = cartitems;
		}


}
