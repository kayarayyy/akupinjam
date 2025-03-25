package com.example.akupinjam.services;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.akupinjam.dto.AuthDto;
import com.example.akupinjam.exceptions.ResourceNotFoundException;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.AuthRepository;
import com.example.akupinjam.utils.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public AuthDto login(String email, String rawPassword) {
        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Wrong password!");
        }

        String token = jwtUtil.generateToken(email, user.getRole());

        return new AuthDto(
            user.getEmail(),
            user.getName(),
            user.getRole(),
            user.isActive(),
            token
        );
    }

    public User register(Map<String, Object> payload, String token) {
        String email = (String) payload.get("email");
        String name = (String) payload.get("name");
        boolean isActive = (boolean) payload.get("is_active");
        String rawPassword = (String) payload.get("password");
        String roleId = (String) payload.get("role_id");

        Role role = jwtUtil.isSuperadmin(token) ? 
            roleService.getRoleById(roleId) : 
            roleService.getRoleByName("customer");

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
            emailService.sendEmail(user.getEmail(), subject, body);
        } else {
            rawPassword = RandomStringUtils.randomAlphanumeric(8);
            emailService.sendInitialPasswordEmail(email, rawPassword);
        }
        user.setPassword(passwordEncoder.encode(rawPassword));

        return authRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found!"));
    }
}
