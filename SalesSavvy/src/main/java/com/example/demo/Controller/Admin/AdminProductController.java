package com.example.demo.Controller.Admin;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Service.Admin.AdminProductService;
import com.example.demo.dto.Admin.AdminProductRequestDTO;
import com.example.demo.dto.Admin.AdminProductResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProductController {

    @Autowired
    private AdminProductService adminProductService;

    
    
    @PostMapping(
    	    value = "/add",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	public ResponseEntity<?> addProduct(
    	        @RequestPart("product") AdminProductRequestDTO dto,
    	        @RequestPart(value = "images", required = false)
    	        List<MultipartFile> images
    	) throws Exception {

    	    AdminProductResponseDTO saved =
    	            adminProductService.addProduct(dto, images);

    	    return ResponseEntity.ok(saved);
    	}


    /* ===================== UPDATE PRODUCT ===================== */
    @PutMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer productId,
            @RequestPart("product") AdminProductRequestDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        Product product = mapToProduct(dto);

        AdminProductResponseDTO updated =
                adminProductService.updateProduct(productId, product, images, dto.getSizes());

        return ResponseEntity.ok(updated);
    }

    /* ===================== DELETE PRODUCT ===================== */
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {

        adminProductService.deleteProduct(productId);
        return ResponseEntity.ok("Product Archieved successfully");
    }

    /* ===================== GET ALL PRODUCTS ===================== */
    @GetMapping("/all")
    public ResponseEntity<List<AdminProductResponseDTO>> getAllProducts() {

        List<AdminProductResponseDTO> products = adminProductService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /* ===================== MAPPER ===================== */
    private Product mapToProduct(AdminProductRequestDTO dto) {

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice().longValue());
        product.setStock(dto.getStock());

        // attach category by ID
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(dto.getCategoryId());
            product.setCategory(category);
        }

        return product;
    }
}

