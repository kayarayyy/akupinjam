package com.example.akupinjam.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.akupinjam.dto.AuthResponseDto;
import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.AuthRepository;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.utils.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseDto login(Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            String rawPassword = (String) payload.get("password");
            User user = authRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Wrong password!!");
            }

            String token = jwtUtil.generateToken(email, user.getRole().getId());
            AuthResponseDto authResponseDto = new AuthResponseDto(
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    user.isActive(),
                    token);
            return new ResponseDto(200, "success", "Login success!", authResponseDto);
        } catch (Exception e) {
            return new ResponseDto(400, "error", e.getMessage(), null);
        }
    }

    public ResponseDto register(Map<String, Object> payload) {
        try {
            User user = new User();

            user.setEmail((String) payload.get("email"));
            user.setName((String) payload.get("name"));
            user.setActive((boolean) payload.get("is_active"));

            String rawPassword = (String) payload.get("password");
            String hashPassword = passwordEncoder.encode(rawPassword);
            user.setPassword(hashPassword);

            int roleId = (int) payload.get("role_id");
            if (!roleRepository.existsById(roleId)) {
                throw new RuntimeException("Role not found!!");
            }

            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found!!"));
            user.setRole(role);

            authRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getId());
            AuthResponseDto authResponseDto = new AuthResponseDto(
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    user.isActive(),
                    token);
            return new ResponseDto(200, "success", "Register success", authResponseDto);
        } catch (Exception e) {
            return new ResponseDto(400, "error", e.getMessage(), null);
        }
    }
}
