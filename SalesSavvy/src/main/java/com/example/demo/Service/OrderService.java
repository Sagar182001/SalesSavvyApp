package com.example.demo.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.User;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;

@Service
public class OrderService {
	

	     private final OrderItemRepository orderItemRepository;
	     
	    
	    private final ProductImageRepository productImageRepository;

	    public OrderService(OrderItemRepository orderItemRepository,
	                        ProductImageRepository productImageRepository) {
	        this.orderItemRepository = orderItemRepository;
	        this.productImageRepository = productImageRepository;
	    }

	    public List<Map<String, Object>> getOrdersForUser(User user) {

	        List<OrderItem> orderItems =
	                orderItemRepository.findSuccessfulOrderItemsByUserId(user.getUser_id());

	        Map<String, Map<String, Object>> orderMap = new HashMap<>();

	        for (OrderItem item : orderItems) {

	            Order order = item.getOrder();
	            if (order == null) continue;

	            // 🔹 Create order block once
	            Map<String, Object> orderDto = orderMap.computeIfAbsent(
	                    order.getOrderId(),
	                    k -> {
	                        Map<String, Object> o = new HashMap<>();
	                        o.put("orderId", order.getOrderId());
	                        o.put("createdAt", order.getCreatedAt());
	                        o.put("totalAmount", order.getTotalAmount());
	                        //o.put("fulfillmentStatus", order.getStatus());
	                        o.put("fulfillmentStatus", order.getFulfillmentStatus());

	                        o.put("items", new ArrayList<>());

	                        // ✅ ADD ADDRESS HERE (🔥 THIS FIXES YOUR ERROR)
	                        if (order.getAddress() != null) {
	                            Map<String, Object> address = new HashMap<>();
	                            address.put("fullName", order.getAddress().getFullName());
	                            address.put("addressType", order.getAddress().getAddressType());
	                            address.put("street", order.getAddress().getStreet());
	                            address.put("city", order.getAddress().getCity());
	                            address.put("state", order.getAddress().getState());
	                            address.put("pincode", order.getAddress().getPincode());
	                            address.put("phone", order.getAddress().getPhone());

	                            o.put("address", address);
	                        }

	                        return o;
	                    }
	            );

	            // 🔹 Product image
	            Product product = item.getProduct();
	            String imageUrl = null;

	            if (product != null) {
	                List<ProductImage> images =
	                        productImageRepository.findByProduct_ProductId(product.getProductId());
	                if (images != null && !images.isEmpty()) {
	                    imageUrl = images.get(0).getImageUri();
	                }
	            }

	            // 🔹 Item block
	            Map<String, Object> itemDto = new HashMap<>();
	            itemDto.put("productName", product != null ? product.getName() : "Product");
	            itemDto.put("quantity", item.getQuantity());
	            itemDto.put("price", item.getPricePerUnit());
	            itemDto.put("image", imageUrl);
	            itemDto.put("size", item.getSize());

	            ((List<Map<String, Object>>) orderDto.get("items")).add(itemDto);
	        }

	        return new ArrayList<>(orderMap.values());
	    }
    
}
