package com.example.demo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.dto.ProductSizeResponseDTO;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public ProductResponseDTO getProductDetails(int productId) {

        Product product = productRepository.findProductWithImages(productId);

        if (product == null || !product.isActive()) {
            throw new RuntimeException("Product not available");
        }

        return mapToDTO(product);
    }


    
    public List<ProductResponseDTO> getProducts(String category, String keyword) {

        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productRepository.searchActiveByNameOrCategory(keyword.trim());
        }
        else if (category != null && !category.trim().isEmpty()) {
            products = productRepository
                    .findByCategory_CategoryNameAndActiveTrue(category.trim());
        }
        else {
            products = productRepository.findByActiveTrue();
        }

        return products.stream()
                       .map(this::mapToDTO)
                       .toList();
    }


    /* Mapping Entity -> DTO */
    private ProductResponseDTO mapToDTO(Product product) {

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(BigDecimal.valueOf(product.getPrice()));
        dto.setStock(product.getStock());

        // category
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        // images
        List<String> images = product.getImages()
                                     .stream()
                                     .map(ProductImage::getImageUri)
                                     .collect(Collectors.toList());

        dto.setImages(images);
        
     // ✅ SIZES (NEW — Step 4B)
        List<ProductSizeResponseDTO> sizeDtos = product.getSizes()
                .stream()
                .map(s -> new ProductSizeResponseDTO(s.getSize(), s.getStock()))
                .toList();

        dto.setSizes(sizeDtos);

        return dto;
    }

}
