package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	// All items for a specific order
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") String orderId);

    // All items for a user where the order status is SUCCESS
    @Query("""
           SELECT oi
           FROM OrderItem oi
           WHERE oi.order.user.user_id = :userId
             AND oi.order.status = 'SUCCESS'
           """)
    List<OrderItem> findSuccessfulOrderItemsByUserId(@Param("userId") int userId);
    
    List<OrderItem> findByOrderOrderId(String orderId);
    
 // batch fetch - avoids N+1
    List<OrderItem> findByOrderOrderIdIn(List<String> orderIds);
    
    boolean existsByProduct_ProductId(Integer productId);
}
