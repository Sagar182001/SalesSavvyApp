package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product, Integer> {
	
	
    List<Product> findByCategory_CategoryId(Integer categoryId);
    
    @Query("""
    		  SELECT p FROM Product p
    		  WHERE LOWER(p.category.categoryName) = LOWER(:category)
    		""")
    List<Product> findByCategoryName(@Param("category") String categoryId);

   
    @Query("""
        SELECT DISTINCT p
        FROM Product p
        LEFT JOIN FETCH p.images
        WHERE p.productId = :productId
    """)
    Product findProductWithImages(int productId);
    
    @Query("""
    	    SELECT p
    	    FROM Product p
    	    JOIN FETCH p.category
    	    LEFT JOIN FETCH p.images
    	    WHERE p.productId = :productId
    	""")
    	Product findByIdWithCategoryAndImages(@Param("productId") Integer productId);
    
    @Query("SELECT p.category.categoryName FROM Product p WHERE p.productId = :productId")
    String findCategoryNameByProductId(@Param("productId") Integer productId);
    
    @Query("""
    	    SELECT DISTINCT p
    	    FROM Product p
    	    LEFT JOIN FETCH p.images
    	    JOIN FETCH p.category
    	    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	       OR LOWER(p.category.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	""")
    	List<Product> searchByNameOrCategory(@Param("keyword") String keyword);
    
    @Query("""
    	    SELECT DISTINCT p
    	    FROM Product p
    	    JOIN FETCH p.category
    	    LEFT JOIN FETCH p.images
    	""")
    	List<Product> findAllWithCategoryAndImages();
    
    @Query("""
    		SELECT DISTINCT p FROM Product p
    		LEFT JOIN FETCH p.category
    		LEFT JOIN FETCH p.images
    		LEFT JOIN FETCH p.sizes
    		WHERE p.productId = :id
    		""")
    		Product findByIdFull(@Param("id") Integer id);

    @Query("""
    		SELECT DISTINCT p FROM Product p
    		LEFT JOIN FETCH p.category
    		LEFT JOIN FETCH p.images
    		LEFT JOIN FETCH p.sizes
    		WHERE p.productId = :productId
    		""")
    		Product findByIdWithCategoryImagesAndSizes(@Param("productId") Integer productId);
    
    List<Product> findByActiveTrue();
    
    
    

    List<Product> findByCategory_CategoryNameAndActiveTrue(String categoryName);

    @Query("""
    	    SELECT p FROM Product p
    	    WHERE p.active = true
    	    AND (
    	        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	        OR LOWER(p.category.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	    )
    	""")
    	List<Product> searchActiveByNameOrCategory(String keyword);

}


