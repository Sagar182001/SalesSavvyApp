package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public User registerUser(User user) {
		
		//Check if Username is exists
		if(( userRepo.findByUsername(user.getUsername())).isPresent()) {
			throw new RuntimeException("Username is already registered or Exits");
		}
		
		//Check if Email is exists
		if(( userRepo.findByEmail(user.getEmail())).isPresent()) {
			throw new RuntimeException("Email is already registered or Exits");
		}
		
		//Encode password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		//Save the user
		return userRepo.save(user);
		
	}
}
