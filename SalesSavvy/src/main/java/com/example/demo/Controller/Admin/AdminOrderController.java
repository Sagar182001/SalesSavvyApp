package com.example.demo.Controller.Admin;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.dto.Admin.AdminFulfillmentStatusRequestDTO;
import com.example.demo.dto.Admin.AdminOrderResponseDTo;
import com.example.demo.Service.Admin.AdminOrderService;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    // ✅ Order list
    @GetMapping
    public ResponseEntity<List<AdminOrderResponseDTo>> getAllOrders() {
        return ResponseEntity.ok(adminOrderService.getAllOrders());
    }

    // ✅ Order details
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(adminOrderService.getOrderById(orderId));
    }

    // ✅ Update fulfillment status
    @PutMapping("/{orderId}/fulfillment-status")
    public ResponseEntity<?> updateFulfillmentStatus(
            @PathVariable String orderId,
            @RequestBody AdminFulfillmentStatusRequestDTO request) {
        return ResponseEntity.ok(
                adminOrderService.updateFulfillmentStatus(orderId, request));
    }
}

