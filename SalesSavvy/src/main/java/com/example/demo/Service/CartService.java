package com.example.demo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.CartItems;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.User;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.CartResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class CartService {
	
	
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    public List<CartResponseDTO> getCartItems(int user_id) {

        // Fetch cart items + product + product images in single query
        List<CartItems> cartItems =
                cartRepository.findCartItemsWithProductDetails(user_id);

        List<CartResponseDTO> response = new ArrayList<>();

        for (CartItems cartItem : cartItems) {

            Product product = cartItem.getProduct();
            List<ProductImage> productImages = product.getImages();   // <-- Direct mapping

            List<String> imageUrls = new ArrayList<>();
            if (productImages != null) {
                for (ProductImage img : productImages) {
                    imageUrls.add(img.getImageUri());
                }
            }

            CartResponseDTO dto = new CartResponseDTO();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getName());
            dto.setQuantity(cartItem.getQuantity());
            dto.setStock(product.getStock());
            dto.setCategoryName(product.getCategory().getCategoryName());
            dto.setImages(imageUrls);
            dto.setPrice(BigDecimal.valueOf(product.getPrice()));
            dto.setSize(cartItem.getSize());


            response.add(dto);
        }

        return response;
    }

    public void addToCart(int user_id, int productId, String size, int quantity) {

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItems> existingItem =
                cartRepository.findByUser_UserIdAndProduct_ProductIdAndSize(
                        user_id, productId, size
                );

        if (existingItem.isPresent()) {
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepository.save(cartItem);
        } else {
            CartItems newItem = new CartItems();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setSize(size);   // 🔥 STORE SIZE
            cartRepository.save(newItem);
        }
    }
 
    public void updateCartItemQuantity(int user_id, int productId, String size, int quantity) {

        Optional<CartItems> existingItem =
                cartRepository.findByUser_UserIdAndProduct_ProductIdAndSize(
                        user_id, productId, size
                );

        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Cart item not found");
        }

        CartItems cartItem = existingItem.get();

        if (quantity <= 0) {
            cartRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        }
    }
    
    public void deleteCartItem(int user_id, int productId, String size) {
    	
    	User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    	
        cartRepository.deleteCartItem(user_id, productId, size);
    }

    @Transactional
    public void clearCart(Integer userId) {
        cartRepository.deleteByUserId(userId);
    }
    
    
}