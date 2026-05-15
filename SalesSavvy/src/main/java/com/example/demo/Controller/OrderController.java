package com.example.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.User;
import com.example.demo.Service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/orders")
public class OrderController {

		private final OrderService orderService;

	    public OrderController(OrderService orderService) {
	        this.orderService = orderService;
	    }

	    
	    
	    @GetMapping
	    public ResponseEntity<List<Map<String, Object>>> getUserOrders(HttpServletRequest request) {

	        User user = (User) request.getAttribute("authenticatedUser");
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        return ResponseEntity.ok(orderService.getOrdersForUser(user));
	    }


	    
}
