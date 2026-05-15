package com.example.demo.Service.Admin;



import com.example.demo.Entity.Order;
import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.OrderStatus;
import com.example.demo.dto.Admin.*;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class AdminBusinessService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public AdminBusinessService(OrderRepository orderRepository,
                                OrderItemRepository orderItemRepository,
                                ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public AdminBusinessResponseDTO calculateMonthlyBusiness(int month, int year) {
        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByMonthAndYear(month, year);
        return calculateBusinessMetrics(successfulOrders);
    }

    @Transactional(readOnly = true)
    public AdminBusinessResponseDTO calculateDailyBusiness(LocalDate date) {
        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByDate(date);
        return calculateBusinessMetrics(successfulOrders);
    }

    @Transactional(readOnly = true)
    public AdminBusinessResponseDTO calculateYearlyBusiness(int year) {
        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByYear(year);
        return calculateBusinessMetrics(successfulOrders);
    }

    @Transactional(readOnly = true)
    public AdminBusinessResponseDTO calculateOverallBusiness() {
    	List<Order> successfulOrders =
    	        orderRepository.findAllByStatus(OrderStatus.SUCCESS);

    	BigDecimal total =
    		    orderRepository.calculateOverallBusiness(OrderStatus.SUCCESS);

        AdminBusinessResponseDTO dto = calculateBusinessMetrics(successfulOrders);
        dto.setTotalRevenue(total == null ? BigDecimal.ZERO : total);
        return dto;
    }

    /**
     * Batched, cached metrics computation:
     * - Uses findByOrderOrderIdIn to fetch all items in one query
     * - Caches productId -> categoryName lookups to avoid repeated DB hits
     */
    private AdminBusinessResponseDTO calculateBusinessMetrics(List<Order> orders) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        Map<String, Integer> categorySales = new HashMap<>();

        if (orders == null || orders.isEmpty()) {
            return new AdminBusinessResponseDTO(BigDecimal.ZERO, categorySales);
        }

        // Sum revenue with BigDecimal
        for (Order order : orders) {
            if (order.getTotalAmount() != 0) {
                totalRevenue = totalRevenue.add(BigDecimal.valueOf(order.getTotalAmount()));
            }
        }

        // Collect all orderIds for batch fetch
        List<String> orderIds = orders.stream()
                .map(Order::getOrderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (orderIds.isEmpty()) {
            return new AdminBusinessResponseDTO(totalRevenue, categorySales);
        }

        // Fetch all OrderItems in a single query to avoid N+1
        List<OrderItem> allItems = orderItemRepository.findByOrderOrderIdIn(orderIds);

        // Cache for productId -> categoryName
        Map<Integer, String> productCategoryCache = new HashMap<>();

        for (OrderItem item : allItems) {
            Integer productId = item.getProduct() != null ? item.getProduct().getProductId() : null;
            String categoryName = "Uncategorized";

            if (productId != null) {
                if (productCategoryCache.containsKey(productId)) {
                    categoryName = productCategoryCache.get(productId);
                } else {
                    try {
                        String cat = productRepository.findCategoryNameByProductId(productId);
                        if (cat != null && !cat.isEmpty()) categoryName = cat;
                    } catch (Exception e) {
                        // fallback: keep "Uncategorized"
                    }
                    productCategoryCache.put(productId, categoryName);
                }
            }

            int qty = (item.getQuantity() == null) ? 0 : item.getQuantity();
            categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + qty);
        }

        return new AdminBusinessResponseDTO(totalRevenue, categorySales);
    }
}

