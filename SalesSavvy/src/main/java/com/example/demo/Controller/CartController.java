package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CartService;
import com.example.demo.dto.CartResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import okhttp3.Response;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    /* === GET CART ITEMS (authenticated user) === */

    @GetMapping("/items")
    public ResponseEntity<List<CartResponseDTO>> getCartItems(
            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        List<CartResponseDTO> response  =
                (List<CartResponseDTO>) cartService.getCartItems(user.getUser_id());

        return ResponseEntity.ok(response);
    }


    /* 1️⃣ ADD TO CART */

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody CartResponseDTO request,
            HttpServletRequest httpRequest) {

        User user = (User) httpRequest.getAttribute("authenticatedUser");

        cartService.addToCart(
                user.getUser_id(),
                request.getProductId(),
                request.getSize(),
                request.getQuantity()
        );

        return ResponseEntity.ok("Item added to cart");
    }


    /* 2️⃣ UPDATE QUANTITY */
      

    @PutMapping("/update")
    public ResponseEntity<?> updateCart(
            @RequestBody CartResponseDTO request,
            HttpServletRequest httpRequest) {

        User user = (User) httpRequest.getAttribute("authenticatedUser");

        cartService.updateCartItemQuantity(
                user.getUser_id(),
                request.getProductId(),
                request.getSize(),
                request.getQuantity()
        );

        return ResponseEntity.ok("Cart updated");
    }


    /* 3️⃣ DELETE ITEM */

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteCart(
            @PathVariable int productId,
            @RequestParam(required = false) String size,
            HttpServletRequest httpRequest) {

        User user = (User) httpRequest.getAttribute("authenticatedUser");

        cartService.deleteCartItem(
                user.getUser_id(),
                productId,
                size
        );

        return ResponseEntity.ok("Item deleted");
    }
}
