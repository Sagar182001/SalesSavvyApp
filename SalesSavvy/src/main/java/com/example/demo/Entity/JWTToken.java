package com.example.demo.Entity;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="jwt_tokens")
public class JWTToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer token_id;
	
	@ManyToOne //Establishing a many-to-one relationship with The User Entity
	@JoinColumn(name="user_id", nullable=false) //Links the tokens to specific user in User Table
	private User user;
	
	@Column(nullable=false) //Ensures that Token can't be null
	private String token;
	
	@Column(nullable = false, updatable = false)
	LocalDateTime created_At = LocalDateTime.now();
	
	@Column(nullable = false)
	LocalDateTime updated_At = LocalDateTime.now();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expires_at",nullable=false) 
	private Date expiresAt;


	public JWTToken(User user, String token, Date expiresAt) {
		super();
		this.user = user;
		this.token = token;
		this.expiresAt = expiresAt;
		this.created_At = LocalDateTime.now();
		this.updated_At = LocalDateTime.now();
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public JWTToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getToken_id() {
		return token_id;
	}

	public void setToken_id(Integer token_id) {
		this.token_id = token_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public Date getExpiresAt() {
		// TODO Auto-generated method stub
		return expiresAt;
	}
	
	
}
