package com.example.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_sizes")
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Integer sizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 10)
    private String size;   // S, M, L, XL, 30, 32, etc

    @Column(nullable = false)
    private Integer stock;

    /* ===== Constructors ===== */

    public ProductSize() {}

    public ProductSize(Product product, String size, Integer stock) {
        this.product = product;
        this.size = size;
        this.stock = stock;
    }

    /*  Getters & Setters  */

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}


