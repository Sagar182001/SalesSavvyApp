package com.example.demo.Controller.Admin;


import com.example.demo.dto.Admin.AdminUserResponse;
import com.example.demo.dto.Admin.AdminUserUpdateRequestDTO;
import com.example.demo.Service.Admin.AdminUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final com.example.demo.Service.Admin.AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // ✅ UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyUser(
            @PathVariable Integer id,
            @RequestBody AdminUserUpdateRequestDTO request) {
        try {
            return ResponseEntity.ok(adminUserService.modifyUser(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong");
        }
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(adminUserService.getUserById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong");
        }
    }

}
