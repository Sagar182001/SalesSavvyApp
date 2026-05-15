package com.example.demo;

import java.security.Key;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jsonwebtoken.security.Keys;

@SpringBootApplication
public class SalesSavvyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesSavvyApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Key jwtSigningKey() {
		return  Keys.hmacShaKeyFor("Your-256-bit-secrete-Your-256-bit-secrete: ".getBytes());
	}

}
