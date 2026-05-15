package com.example.demo.dto.Admin;

import java.sql.Timestamp;

public class AdminOrderResponseDTo {
	
	private String orderId;
    private Integer userId;
    private double totalAmount;
    private String paymentStatus;
    private String fulfillmentStatus;
    private Timestamp createdAt;
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
    
    
}
