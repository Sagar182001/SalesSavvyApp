package com.example.demo.Service;


import java.nio.charset.StandardCharsets;
import java.security.Key;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.JWTToken;
import com.example.demo.Entity.User;
import com.example.demo.Repository.JWTTokenRepository;
import com.example.demo.Repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    private Key SIGNING_KEY;

    private final UserRepository userRepo;
    private final JWTTokenRepository jwtTokenRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(
            UserRepository userRepo,
            JWTTokenRepository jwtTokenRepo,
            @Value("${jwt.secret}") String jwtSecret
    ) {
    	
        this.userRepo = userRepo;
        this.jwtTokenRepo = jwtTokenRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();

        // Ensure key length is at least 64 bytes for HS512
        if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
            throw new IllegalArgumentException("jwt.secret in application.properties must be at least 64 bytes long for HS512");
        }

        this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    
     // -------------------- Authenticate User --------------------
    public User authenticate(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }

    // -------------------- Generate or Reuse Token --------------------
    public String generateToken(User user) {

        Date now = new Date();

        JWTToken existingToken = jwtTokenRepo.findByUserId(user.getUser_id());

        String token;

        // Reuse token if not expired
        if (existingToken != null && now.before(existingToken.getExpiresAt())) {
            token = existingToken.getToken();
        } 
        else {
            // Delete old token if exists
            if (existingToken != null) {
                jwtTokenRepo.delete(existingToken);
            }

            token = generateNewToken(user);
            saveToken(user, token);
        }

        return token;
    }

    // -------------------- Save Token --------------------
    private void saveToken(User user, String token) {
        Date expiresAt = new Date(System.currentTimeMillis() + 18000000);  // 5 hour expiry
        JWTToken jwtToken = new JWTToken(user, token, expiresAt);
        jwtTokenRepo.save(jwtToken);
    }

    // -------------------- Generate New JWT Token --------------------
    private String generateNewToken(User user) {

        Date expiry = new Date(System.currentTimeMillis() + 18000000); // 5 hour

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

	
	
	public boolean validateToken(String token) {
	    try {
	        System.err.println("VALIDATING TOKEN...");

	        // Parse and validate the token
	        Jwts.parserBuilder()
	                .setSigningKey(SIGNING_KEY)
	                .build()
	                .parseClaimsJws(token);

	        // Check if the token exists in the database and is not expired
	        Optional<JWTToken> jwtToken = jwtTokenRepo.findByToken(token);

	        if (jwtToken.isPresent()) {
	            System.err.println("Token Expiry: " + jwtToken.get().getExpiresAt());
	            System.err.println("Current Time: " + new Date());

	            return jwtToken.get().getExpiresAt().after(new Date());
	        }

	        return false;

	    } catch (Exception e) {
	        System.err.println("Token validation failed: " + e.getMessage());
	        return false;
	    }
	}

	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		
		return Jwts.parserBuilder()
				.setSigningKey(SIGNING_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	
	public void logout(String token, HttpServletResponse response) {

	    if (token != null && !token.isBlank()) {
	        // remove token from DB
	        jwtTokenRepo.findByToken(token).ifPresent(jwtTokenRepo::delete);
	    }

	    // delete cookie from browser
	    Cookie cookie = new Cookie("authToken", "");
	    cookie.setPath("/");
	    cookie.setHttpOnly(true);
	    cookie.setSecure(false);        // set true if you use HTTPS
	    cookie.setMaxAge(0);

	    response.addCookie(cookie);
	}

}