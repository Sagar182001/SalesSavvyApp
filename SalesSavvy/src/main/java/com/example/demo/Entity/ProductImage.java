
package com.example.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productImages")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUri;

    // Getters and Setters
    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // Default constructor
    public ProductImage() {
        // TODO Auto-generated constructor stub
    }

    // Constructor with all fields
    public ProductImage(Integer imageId, Product product, String imageUri) {
        super();
        this.imageId = imageId;
        this.product = product;
        this.imageUri = imageUri;
    }

    // Constructor without imageId (auto-generated)
    public ProductImage(Product product, String imageUri) {
        super();
        this.product = product;
        this.imageUri = imageUri;
    }
}