package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.CartItems;
import com.example.demo.Entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartItems, Integer> {
	 
	
		@Query("SELECT c FROM CartItems c WHERE c.user.user_id = :userId AND c.product.productId = :productId")
		Optional<CartItems> findByUserAndProduct(int userId, int productId);
		
		@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItems c WHERE c.user.user_id = :userId")
		int countTotalItems(int userId);
		
		@Query("""
		       SELECT c 
		       FROM CartItems c 
		       JOIN FETCH c.product p 
		       LEFT JOIN FETCH p.images 
		       WHERE c.user.user_id = :userId
		       """)
		List<CartItems> findCartItemsWithProductDetails(int userId);
		
		@Modifying
		@Transactional
		@Query("""
		    DELETE FROM CartItems c
		    WHERE c.user.user_id = :userId
		      AND c.product.productId = :productId
		      AND (
					  (:size IS NULL AND c.size IS NULL)
			          OR c.size = :size
				  )
		""")
		void deleteCartItem(int userId, int productId, String size);

		
		@Modifying
		@Transactional
		@Query("DELETE FROM CartItems c WHERE c.user.user_id = :userId")
		void deleteByUserId(int userId);
		
		@Query("""
			    SELECT c FROM CartItems c
			    WHERE c.user.user_id = :userId
			      AND c.product.productId = :productId
			      AND c.size = :size
			""")
		Optional<CartItems> findByUser_UserIdAndProduct_ProductIdAndSize(
		        int userId,
		        int productId,
		        String size
		);
		
		 boolean existsByProduct_ProductId(Integer productId);
		

}

		
