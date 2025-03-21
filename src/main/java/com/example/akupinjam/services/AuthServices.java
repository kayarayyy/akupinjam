package com.example.akupinjam.services;

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

@Service
public class AuthServices {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseDto login(Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String rawPassword = (String) payload.get("password");
        User user = authRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Wrong password!!");
        }
        AuthResponseDto authResponseDto = new AuthResponseDto(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getAlamat(),
                user.getNip() != null ? user.getNip() : 0);
        return new ResponseDto("success", "Login success!", authResponseDto);
    }

    public ResponseDto register(Map<String, Object> payload) {
        User user = new User();

        user.setEmail((String) payload.get("email"));
        user.setName((String) payload.get("name"));

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

        AuthResponseDto authResponseDto = new AuthResponseDto(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getAlamat(),
                user.getNip() != null ? user.getNip() : 0);
        return new ResponseDto("success", "Register success!", authResponseDto);
    }
}
