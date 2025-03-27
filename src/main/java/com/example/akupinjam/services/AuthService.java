package com.example.akupinjam.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.akupinjam.dto.AuthDto;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.AuthRepository;
import com.example.akupinjam.security.CustomUserDetails;
import com.example.akupinjam.utils.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthDto login(String email, String rawPassword) {
        User user = userService.getUserByEmail(email);

        validatePassword(rawPassword, user.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),
                        rawPassword));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = String.valueOf(userDetails.getUser().getRole().getName());
        List<String> features = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtil.generateToken(authentication);

        return new AuthDto(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.isActive(),
                token,
                features);
    }

    @Transactional
    public User register(Map<String, Object> payload, String token) {
        // Validasi input agar tidak null
        String email = Objects.toString(payload.get("email"), "").trim();
        String name = Objects.toString(payload.get("name"), "").trim();
        String rawPassword = Objects.toString(payload.get("password"), "").trim();
        String roleId = Objects.toString(payload.get("role_id"), "").trim();
        boolean isActive = Boolean.parseBoolean(Objects.toString(payload.get("is_active"), "false"));
        System.out.println(name);
        
        if (email.isEmpty() || name.isEmpty() || rawPassword.isEmpty()) {
            System.out.println(name+"isempty");
            throw new IllegalArgumentException("Email, name, and password must not be empty");
        }
        System.out.println(name);
        System.out.println(jwtUtil.isSuperadmin(token));
        
        // Jika token null, default role adalah "customer"
        Role role = (token != null && jwtUtil.isSuperadmin(token))
        ? roleService.getRoleById(roleId)
        : roleService.getRoleByName("CUSTOMER");
        System.out.println(name);
        
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setActive(isActive);
        user.setRole(role);
        user.setPassword(rawPassword);
        
        if (role.getName().equals("CUSTOMER")) {
            // System.out.println(name+"customer");
            userService.createUser(user);
            
            sendCustomerRegistrationEmail(user);
        } else {
            // System.out.println(name);
            rawPassword = RandomStringUtils.randomAlphanumeric(8);
            user.setPassword(passwordEncoder.encode(rawPassword));
            userService.createUser(user);
            emailService.sendInitialPasswordEmail(email, rawPassword);
        }

        return user;

    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    private void sendCustomerRegistrationEmail(User user) {
        String subject = "Registrasi Akun Berhasil - AKuPinjam";
        String body = generateRegistrationEmailBody(user.getName(), user.getEmail());
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String generateRegistrationEmailBody(String name, String email) {
        return String.format(
                "Halo " + name + ",\n\n" +
                        "Selamat! Akun Anda telah berhasil dibuat di AKuPinjam.\n\n" +
                        "Berikut adalah detail akun Anda:\n" +
                        "Nama: " + name + "\n" +
                        "Email: " + email + "\n\n" +
                        "Anda sekarang dapat menggunakan layanan kami. Jika ada pertanyaan, hubungi support kami.\n\n" +
                        "Terima kasih,\n" +
                        "Tim AKuPinjam");
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("Wrong password!");
        }
    }
}
