package com.example.akupinjam.services;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.akupinjam.dto.AuthDto;
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

    @Autowired
    private EmailService emailService;

    public ResponseDto login(Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            String rawPassword = (String) payload.get("password");
            User user = authRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                return new ResponseDto(401, "failed", "Wrong password!", null);
            }

            String token = jwtUtil.generateToken(email, user.getRole());
            AuthDto authResponseDto = new AuthDto(
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

    public ResponseDto register(Map<String, Object> payload, String token) {
        try {
            String email = (String) payload.get("email");
            String name = (String) payload.get("name");
            boolean isActive = (boolean) payload.get("is_active");
            String rawPassword = (String) payload.get("password");
            String roleId = (String) payload.get("role_id");

            // Variabel untuk menyimpan role
            Role role;
            try {

                // Jika role dari token adalah Super Admin, pilih role sesuai input
                if (jwtUtil.isSuperadmin(token)) {
                    role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found!"));
                } else {
                    // Jika bukan Super Admin, tetapkan role sebagai Customer
                    role = roleRepository.findByName("customer")
                            .orElseThrow(() -> new RuntimeException("Customer role not found!"));
                }
            } catch (Exception e) {
                // Jika tidak ada token atau token tidak valid, tetapkan sebagai Customer
                role = roleRepository.findByName("customer")
                        .orElseThrow(() -> new RuntimeException("Customer role not found!"));
            }

            // For create role w/out auth
            // Role role = roleRepository.findById(roleId)
            // .orElseThrow(() -> new RuntimeException("Role not found!"));

            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setActive(isActive);
            user.setRole(role);

            if (role.getName().equals("customer")) {
                String subject = "Registrasi Akun Berhasil - AKuPinjam";
                String body = "Selamat, akun Anda telah dibuat.\n\n"
                        + "Berikut adalah detail akun Anda:\n"
                        + "Nama: " + name + "\n"
                        + "Email: " + email + "\n"
                        + "Terima kasih.";
                emailService.sendEmail(user.getEmail(), subject,
                        body);
            } else {
                rawPassword = RandomStringUtils.randomAlphanumeric(8);
                emailService.sendInitialPasswordEmail(email, rawPassword);
            }
            user.setPassword(passwordEncoder.encode(rawPassword));

            authRepository.save(user);

            AuthDto authResponseDto = new AuthDto(
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    user.isActive(),
                    null);

            return new ResponseDto(200, "success", "Register success", authResponseDto);

        } catch (Exception e) {
            return new ResponseDto(400, "error", "Registration failed: " + e.getMessage(), null);
        }
    }
}
