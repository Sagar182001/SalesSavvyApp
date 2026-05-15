package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;
import com.example.demo.Service.AuthService;
import com.example.demo.dto.Authentication.LoginRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173",
allowCredentials = "true")

@RequestMapping("/api/auth")
public class AuthController {
	
		private final AuthService authServ;
	
		public AuthController(AuthService authServ) {
			super();
			this.authServ = authServ;
		}
		
		@PostMapping("/login")
		public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
			
			 /* try {
				User user = authServ.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
				String token = authServ.generateToken(user);
				
				 Cookie cookie = new Cookie("authToken", token);
				cookie.setHttpOnly(true);
				cookie.setSecure(false); //Set to true if using HTTPS
				cookie.setPath("/");
				cookie.setMaxAge(3600); //1hr
				//cookie.setDomain("localhost");
				response.addCookie(cookie);
				
				response.addHeader(
					    "Set-Cookie",
					    "authToken=" + token +
					    "; Path=/" +
					    "; Max-Age=3600" +
					    "; HttpOnly" +
					    "; SameSite=None"
				);

				*/
			try {
	            User user = authServ.authenticate(
	                    loginRequest.getUsername(),
	                    loginRequest.getPassword()
	            );

	            String token = authServ.generateToken(user);

	            // 🔥🔥🔥 THIS IS THE FIX (DO NOT CHANGE)
	            String cookieHeader =
	                    "authToken=" + token +
	                    "; Path=/" +
	                    "; Domain=localhost" + 		
	                    "; Max-Age=3600" +
	                    "; HttpOnly" +
	                    "; SameSite=Lax";

	            response.addHeader("Set-Cookie", cookieHeader);

				//Optional but useful 
				 //response.addHeader("Set-Cookie", String.format("authToken=%s; HttpOnly: Path=/; Max-Age=3600;SameSite=None",token));
				 
				 Map<String, Object> responseBody = new HashMap<>();
				 responseBody.put("message", "Login Successful");
				 responseBody.put("role", user.getRole().name());
				 responseBody.put("userId", user.getUser_id());   // 🔥 ADD THIS
				 responseBody.put("username", user.getUsername());
				 
				 return ResponseEntity.ok(responseBody);
				 
			} catch (RuntimeException e) {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
				
			}
			
		}
		
		@PostMapping("/logout")
		public ResponseEntity<?> logout(
		        HttpServletRequest request,
		        HttpServletResponse response) {
	
		    String token = null;
	
		    Cookie[] cookies = request.getCookies();
		    if (cookies != null) {
		        for (Cookie cookie : cookies) {
		            if ("authToken".equals(cookie.getName())) {
		                token = cookie.getValue();
		                break;
		            }
		        }
		    }
	
		    authServ.logout(token, response);
	
		    return ResponseEntity.ok(Map.of(
		            "message", "Logout successful",
		            "redirect", "/login"
		    ));
		}

}
