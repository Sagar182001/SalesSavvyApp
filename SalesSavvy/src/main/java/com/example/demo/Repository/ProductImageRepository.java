package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository <ProductImage, Integer> {

	List<ProductImage> findByProduct_ProductId(Integer productId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ProductImage pi WHERE pi.product.productId = :productId")
	void deleteBYProductId(Integer productId);
	
	
	@Query("SELECT p.category.categoryName FROM Product p WHERE p.productId = :productId")
	String findCategoryNameByProductId(@Param("productId") Integer productId);

}


