package com.example.demo.Service.Admin;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.ProductSize;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ProductSizeRepository;
import com.example.demo.dto.ProductSizeResponseDTO;
import com.example.demo.dto.Admin.AdminProductRequestDTO;
import com.example.demo.dto.Admin.AdminProductResponseDTO;
import com.example.demo.dto.Admin.ProductSizeRequestDTO;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminProductService {

	@Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductImageRepository productImageRepo;
    
    @Autowired
    private CategoryRepository categoryRepo;
    
    @Autowired
    private ProductSizeRepository productSizeRepo;
    
    @Autowired
    private OrderItemRepository orderItemRepo;;
    
    @Autowired
    private CartRepository cartRepo;
    


    private final String UPLOAD_DIR = "uploads/";
    
    @Transactional
    public AdminProductResponseDTO addProduct(
            AdminProductRequestDTO dto,
            List<MultipartFile> images
    ) throws Exception {

        Product product = new Product();

        // 1️⃣ BASIC FIELDS
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice().longValue());

        // 2️⃣ CATEGORY (MANDATORY)
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        // 3️⃣ STOCK DECISION (THIS IS THE CORE FIX)
        if (dto.getSizes() != null && !dto.getSizes().isEmpty()) {
            // SIZE-BASED (shirts, shoes)
            product.setStock(null);
        } else {
            // NON-SIZE (mobiles, electronics)
            product.setStock(dto.getStock());
        }

        // 4️⃣ SAVE PRODUCT FIRST
        Product savedProduct = productRepo.save(product);

        // 5️⃣ SAVE IMAGES
        if (images != null && !images.isEmpty()) {
            saveImages(savedProduct, images);
        }

        // 6️⃣ SAVE SIZES (ONLY IF PRESENT)
        if (dto.getSizes() != null && !dto.getSizes().isEmpty()) {
            saveProductSizes(savedProduct, dto.getSizes());
        }

        // 7️⃣ RELOAD & RETURN
        Product reloaded =
                productRepo.findByIdWithCategoryImagesAndSizes(savedProduct.getProductId());

        return ToAdminDTO(reloaded);
    }

    
    
    @Transactional
    public AdminProductResponseDTO updateProduct(
            Integer productId,
            Product updatedProduct,
            List<MultipartFile> images,
            List<ProductSizeRequestDTO> sizes
    ) throws Exception {

        // 1️⃣ LOAD EXISTING PRODUCT
        Product existing = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2️⃣ LOAD REAL CATEGORY FROM DB (MANDATORY)
        if (updatedProduct.getCategory() != null &&
            updatedProduct.getCategory().getCategoryId() != null) {

            Integer categoryId = updatedProduct.getCategory().getCategoryId();

            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            existing.setCategory(category);
        }

        // 3️⃣ BASIC FIELDS
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());

        // 4️⃣ 🔥 STOCK DECISION (CORRECT & FINAL)
        if (sizes != null && !sizes.isEmpty()) {
            // SIZE-BASED PRODUCT (shirts, shoes)
            existing.setStock(null);
        } else {
            // NON-SIZE PRODUCT (mobiles, electronics)
            existing.setStock(updatedProduct.getStock());
        }

        // 5️⃣ SAVE PRODUCT FIRST
        Product saved = productRepo.save(existing);

        // 6️⃣ UPDATE IMAGES (IF PROVIDED)
        if (images != null && !images.isEmpty()) {
            productImageRepo.deleteBYProductId(productId);
            saveImages(saved, images);
        }

        // 7️⃣ ✅ UPDATE SIZES (NO DELETE, NO DUPLICATES)
        if (sizes != null && !sizes.isEmpty()) {
            saveProductSizes(saved, sizes);
        }

        // 8️⃣ RELOAD & RETURN
        Product reloaded =
                productRepo.findByIdFull(saved.getProductId());

        return ToAdminDTO(reloaded);
    }

    /* DELETE PRODUCT 
    @Transactional
    public void deleteProduct(Integer productId) {
    	//delete product image first
        productImageRepo.deleteBYProductId(productId);
        
        //ten delete productId
        productRepo.deleteById(productId);
    } */
    
    @Transactional
    public void deleteProduct(Integer productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔒 RULE 1: Product ever ordered → NEVER delete
        if (orderItemRepo.existsByProduct_ProductId(productId)) {
            throw new RuntimeException(
                "Product already ordered. Cannot delete."
            );
        }

        // 🔒 RULE 2: Product in any cart → do not delete
        if (cartRepo.existsByProduct_ProductId(productId)) {
            throw new RuntimeException(
                "Product exists in carts. Cannot delete."
            );
        }

        // ✅ SOFT DELETE
        product.setActive(false);
        productRepo.save(product);
    }

    
    
    /* ================= SAVE / UPDATE PRODUCT SIZES ================= */
    private void saveProductSizes(Product product, List<ProductSizeRequestDTO> sizes) {

        if (sizes == null || sizes.isEmpty()) return;

        for (ProductSizeRequestDTO dto : sizes) {

            // 🔍 Check if size already exists for this product
            ProductSize existing =
                    productSizeRepo.findByProduct_ProductIdAndSize(
                            product.getProductId(),
                            dto.getSize()
                    ).orElse(null);

            if (existing != null) {
                // ✅ UPDATE EXISTING SIZE
                existing.setStock(dto.getStock());
                product.getSizes().add(existing);
                productSizeRepo.save(existing);

            } else {
                // ✅ ADD NEW SIZE
                ProductSize ps = new ProductSize();
                ps.setProduct(product);
                ps.setSize(dto.getSize());
                ps.setStock(dto.getStock());
                product.getSizes().add(ps);
                productSizeRepo.save(ps);
            }
        }
    }

    /* ===================== GET ALL PRODUCTS ===================== */
    public List<AdminProductResponseDTO> getAllProducts() {

    	List<Product> products = productRepo.findAllWithCategoryAndImages();

        List<AdminProductResponseDTO> list = new ArrayList<>();

        for (Product product : products) {
            list.add(ToAdminDTO(product));
        }

        return list;
    }

    private String sanitizeFileName(String originalName) {
        return originalName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
    }
    
    private void saveImages(Product product, List<MultipartFile> images) throws Exception {

        if (images == null || images.isEmpty()) return;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        Files.createDirectories(uploadPath);

        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            if (originalName == null) originalName = "image";

            String safeName = sanitizeFileName(originalName);
            String filename = UUID.randomUUID() + "_" + safeName;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/" + filename;

            ProductImage img = new ProductImage();
            img.setProduct(product);
            img.setImageUri(imageUrl);
            productImageRepo.save(img);

            product.getImages().add(img);
        }
    }

    /* ================= CONVERTER ================= */
    private AdminProductResponseDTO ToAdminDTO(Product product) {

        List<String> images = product.getImages()
                .stream()
                .map(ProductImage::getImageUri)
                .toList();

        AdminProductResponseDTO dto = new AdminProductResponseDTO();
        
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(BigDecimal.valueOf(product.getPrice()));             // long from entity
        dto.setStock(product.getStock());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
        	dto.setCategoryId(product.getCategory().getCategoryId());
            dto.setCategoryName(product.getCategory().getCategoryName());
            // if your AdminProductResponseDTO has categoryId, set it here:
            // dto.setCategoryId(product.getCategory().getCategoryId());
        }

        List<String> urls = new ArrayList<>();
        if (product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                urls.add(img.getImageUri());          // ✅ URL, not Base64
            }
        }
        dto.setImages(urls);
        
        if (product.getSizes() != null) {
            List<ProductSizeResponseDTO> sizeDtos = product.getSizes()
                    .stream()
                    .map(s -> new ProductSizeResponseDTO(s.getSize(), s.getStock()))
                    .toList();
            dto.setSizes(sizeDtos);
        }

        return dto;
    }
}


