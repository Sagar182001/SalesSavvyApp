package com.example.demo.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_id;
	
	@Column(nullable = false, unique=true)
	String username;
	
	@Column(nullable = false, unique=true)
	String email;
	
	@Column(nullable = false)
	String password;
	
	@Enumerated(EnumType.STRING)
	Role role;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	LocalDateTime created_At = LocalDateTime.now();
	
	@UpdateTimestamp
	@Column(nullable = false)
	LocalDateTime updated_At = LocalDateTime.now();
	

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getCreated_At() {
		return created_At;
	}

	public void setCreated_At(LocalDateTime created_At) {
		this.created_At = created_At;
	}

	public LocalDateTime getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(LocalDateTime updated_At) {
		this.updated_At = updated_At;
	}
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Integer user_id, String username, String email, String password, Role role, LocalDateTime created_At,
			LocalDateTime updated_At) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.created_At = created_At;
		this.updated_At = updated_At;
	}

	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
