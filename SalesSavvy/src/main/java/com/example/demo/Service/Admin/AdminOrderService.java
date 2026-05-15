package com.example.demo.Service.Admin;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.FulfillmentStatus;
import com.example.demo.Entity.Order;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.dto.*;
import com.example.demo.dto.Admin.AdminFulfillmentStatusRequestDTO;
import com.example.demo.dto.Admin.AdminOrderDetailResponseDTO;
import com.example.demo.dto.Admin.AdminOrderItemResponseDTO;
import com.example.demo.dto.Admin.AdminOrderResponseDTo;

@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;

    public AdminOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ Get all orders
    public List<AdminOrderResponseDTo> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get order details
    public AdminOrderDetailResponseDTO getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return mapToOrderDetail(order);
    }

    // ✅ Update fulfillment status ONLY
    public AdminOrderResponseDTo updateFulfillmentStatus(
            String orderId,
            AdminFulfillmentStatusRequestDTO request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        try {
            FulfillmentStatus status =
                    FulfillmentStatus.valueOf(request.getFulfillmentStatus().toUpperCase());
            order.setFulfillmentStatus(status);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid fulfillment status");
        }

        return mapToOrderResponse(orderRepository.save(order));
    }

    // =========================
    // MAPPERS
    // =========================

    private AdminOrderResponseDTo mapToOrderResponse(Order order) {
        AdminOrderResponseDTo dto = new AdminOrderResponseDTo();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUser_id());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentStatus(order.getStatus().name());
        dto.setFulfillmentStatus(order.getFulfillmentStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    private AdminOrderDetailResponseDTO mapToOrderDetail(Order order) {
        AdminOrderDetailResponseDTO dto = new AdminOrderDetailResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUser_id());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentStatus(order.getStatus().name());
        dto.setFulfillmentStatus(order.getFulfillmentStatus().name());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setItems(
                order.getItems().stream().map(item -> {
                    AdminOrderItemResponseDTO i = new AdminOrderItemResponseDTO();
                    i.setProductId(item.getProduct().getProductId());
                    i.setProductName(item.getProduct().getName());
                    i.setQuantity(item.getQuantity());
                    i.setPricePerUnit(item.getPricePerUnit());
                    i.setTotalPrice(item.getTotalPrice());
                    return i;
                }).collect(Collectors.toList())
        );
        return dto;
    }

}
