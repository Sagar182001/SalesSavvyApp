
package com.example.demo.Controller;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Service.ProductService;
import com.example.demo.dto.ProductResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(
            productService.getProducts(category, keyword)
        );
    }


    /* To Get single product */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) {

        ProductResponseDTO product = productService.getProductDetails(id);
        return ResponseEntity.ok(product);
    }
    
}
