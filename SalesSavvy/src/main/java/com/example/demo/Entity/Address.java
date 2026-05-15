package com.example.demo.Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String fullName;
	    private String phone;
	    private String pincode;
	    private String state;
	    private String city;
	    private String street;
	    private String landmark;
	    private String addressType;  

	    @ManyToOne
	    @JoinColumn(name = "user_id")   
	    private User user;

	    // ===== Getters and Setters =====
	    public Long getId() {
	        return id;
	    }
	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getFullName() {
	        return fullName;
	    }
	    public void setFullName(String fullName) {
	        this.fullName = fullName;
	    }

	    public String getPhone() {
	        return phone;
	    }
	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getPincode() {
	        return pincode;
	    }
	    public void setPincode(String pincode) {
	        this.pincode = pincode;
	    }

	    public String getState() {
	        return state;
	    }
	    public void setState(String state) {
	        this.state = state;
	    }

	    public String getCity() {
	        return city;
	    }
	    public void setCity(String city) {
	        this.city = city;
	    }

	    public String getStreet() {
	        return street;
	    }
	    public void setStreet(String street) {
	        this.street = street;
	    }

	    public String getLandmark() {
	        return landmark;
	    }
	    public void setLandmark(String landmark) {
	        this.landmark = landmark;
	    }

	    public String getAddressType() {
	        return addressType;
	    }
	    public void setAddressType(String addressType) {
	        this.addressType = addressType;
	    }

	    public User getUser() {
	        return user;
	    }
	    public void setUser(User user) {
	        this.user = user;
	    }
	
}
