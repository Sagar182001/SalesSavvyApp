package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
public class Order {
	
    @Id
    @Column(name = "order_id", nullable = false)
    private String orderId;   // Razorpay order

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "created_at", nullable = false, updatable=false)
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private  Timestamp updatedAt;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfillmentStatus fulfillmentStatus = FulfillmentStatus.CREATED;
    
    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // =======================
    // GETTERS & SETTERS
    // =======================

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
	
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public List<OrderItem> getItems() {
        return items;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public FulfillmentStatus getFulfillmentStatus() {
		return fulfillmentStatus;
	}
	public void setFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
		this.fulfillmentStatus = fulfillmentStatus;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
    
}

