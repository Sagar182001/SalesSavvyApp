package com.example.demo.Controller;



import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import com.example.demo.dto.Admin.AdminRegisterRequest;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {
	
	private final UserService userServ;
	
	@Autowired	
	public UserController(UserService userServ) {
		super();
		this.userServ = userServ;
	}


	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			
			if (user.getRole() != null && user.getRole().name().equals("ADMIN")) {
	            return ResponseEntity.badRequest()
	                    .body(Map.of("error", "You cannot create admin from public registration"));
	        }

	        user.setRole(Role.CUSTOMER);  // force CUSTOMER
	        
			User registeredUser = userServ.registerUser(user);
			return ResponseEntity.ok(Map.of("message", 
					"User registered successufully", "user", registeredUser));
		} catch(RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		
	}
	
	private static final String ADMIN_SECRET = "SALES-SAVVY-2025";

	@PostMapping("/admin/register")
	public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterRequest request) {
		System.out.println("DEBUG Admin request: username=" + request.getUsername()
        + " email=" + request.getEmail()
        + " secret=" + request.getSecret());
		
	    try {
	        if (!ADMIN_SECRET.equals(request.getSecret())) {
	            return ResponseEntity.badRequest()
	                    .body(Map.of("error", "Invalid admin secret key"));
	        }

	        User user = new User();
	        user.setUsername(request.getUsername());
	        user.setEmail(request.getEmail());
	        user.setPassword(request.getPassword());
	        user.setRole(Role.ADMIN);

	        User registeredAdmin = userServ.registerUser(user);

	        return ResponseEntity.ok(Map.of(
	                "message", "Admin created successfully",
	                "user", registeredAdmin
	        ));

	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	    }
	}

}
