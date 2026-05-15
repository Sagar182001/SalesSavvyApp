package com.example.demo.Controller;


import com.example.demo.Service.PaymentService;
import com.example.demo.dto.CartResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	    private final PaymentService paymentService;
	
	    public PaymentController(PaymentService paymentService) {
	        this.paymentService = paymentService;
	    }
	
	    // 1️⃣ Create Razorpay Order
	    @PostMapping("/create")
	    public ResponseEntity<?> createOrder(
	            @RequestParam int userId,
	            @RequestParam int addressId,
	            @RequestParam double totalAmount,
	            @RequestBody List<CartResponseDTO> cartItems) {
	
	        try {
	            String razorpayOrderId =
	                    paymentService.createOrder(userId, addressId, totalAmount, cartItems);
	
	            return ResponseEntity.ok(Map.of(
	                    "razorpayOrderId", razorpayOrderId
	            ));
	
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError().body(Map.of(
	                    "message", "Error creating Razorpay order",
	                    "error", e.getMessage()
	            ));
	        }
	    }
	
	
	    // 2️⃣ Verify Payment
	    @PostMapping("/verify")
	    public ResponseEntity<?> verifyPayment(
	            @RequestParam String razorpayOrderId,
	            @RequestParam String razorpayPaymentId,
	            @RequestParam String razorpaySignature,
	            @RequestBody List<CartResponseDTO> cartItems) {
	
	        try {
	            String result =
	                    paymentService.verifyPayment(
	                            razorpayOrderId,
	                            razorpayPaymentId,
	                            razorpaySignature,
	                            cartItems
	                    );
	
	            return ResponseEntity.ok(Map.of("message", result));
	
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(Map.of(
	                    "message", "Payment verification failed",
	                    "error", e.getMessage()
	            ));
	        }
	    }
	    
}

