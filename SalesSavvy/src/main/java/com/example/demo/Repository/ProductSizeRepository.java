package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.ProductSize;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Integer> {
	
	List<ProductSize> findByProduct_ProductId(Integer productId);

    void deleteByProduct_ProductId(Integer productId);
    
    Optional<ProductSize> findByProduct_ProductIdAndSize(
            Integer productId,
            String size
    );
    
   
	
}
