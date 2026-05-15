package com.example.demo.dto.Admin;

import java.sql.Timestamp;
import java.util.List;

public class AdminOrderDetailResponseDTO {
	
	private String orderId;
    private Integer userId;
    private double totalAmount;
    private String paymentStatus;
    private String fulfillmentStatus;
    private Timestamp createdAt;
    private List<AdminOrderItemResponseDTO> items;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getFulfillmentStatus() {
		return fulfillmentStatus;
	}
	public void setFulfillmentStatus(String fulfillmentStatus) {
		this.fulfillmentStatus = fulfillmentStatus;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public List<AdminOrderItemResponseDTO> getItems() {
		return items;
	}
	public void setItems(List<AdminOrderItemResponseDTO> items) {
		this.items = items;
	}
    
    
}
