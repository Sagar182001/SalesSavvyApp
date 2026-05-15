package com.example.demo.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart_items")
public class CartItems {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Integer id;
	
	@ManyToOne //Establishing a many-to-one relationship with The User Entity
	@JoinColumn(name="user_id", nullable=false) //Links the tokens to specific user in User Table
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
	
	@Column(name="quantity")
	private Integer quantity;
	
	// 🔥 ADD THIS
    private String size;

    // getters & setters
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public CartItems(User user, Product product, Integer quantity) {
		super();
		this.user = user;
		this.product = product;
		this.quantity = quantity;
	}

	public CartItems get() {
		// TODO Auto-generated method stub
		return null;
	}

	public CartItems() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
