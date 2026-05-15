package com.example.demo.Service.Admin;


import com.example.demo.dto.Admin.AdminUserResponse;
import com.example.demo.dto.Admin.AdminUserUpdateRequestDTO;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.JWTTokenRepository;
import com.example.demo.Repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final JWTTokenRepository jwtTokenRepository;

    public AdminUserService(UserRepository userRepository,
                            JWTTokenRepository jwtTokenRepository) {
        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    // ✅ MODIFY USER
    @Transactional
    public AdminUserResponse modifyUser(Integer userId,
                                        AdminUserUpdateRequestDTO request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
            }
        }

        // ✅ IMPORTANT SECURITY STEP
        jwtTokenRepository.deleteByUserId(user.getUser_id());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    // ✅ GET USER BY ID
    public AdminUserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return mapToResponse(user);
    }

    // ✅ ENTITY → DTO MAPPER
    private AdminUserResponse mapToResponse(User user) {
        AdminUserResponse dto = new AdminUserResponse();
        dto.setUserId(user.getUser_id());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setCreatedAt(user.getCreated_At());
        dto.setUpdatedAt(user.getUpdated_At());
        return dto;
    }

}
