package com.example.akupinjam.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.akupinjam.dto.AuthDto;
import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.exceptions.ResourceNotFoundException;
import com.example.akupinjam.models.User;
import com.example.akupinjam.services.AuthService;
import com.example.akupinjam.services.ResetPasswordService;
import com.example.akupinjam.utils.ResponseUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ResetPasswordService resetPasswordService;

    // @Secured("MANAGE_USERS")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody Map<String, Object> payload) {
        AuthDto authDto = authService.login(
                (String) payload.get("email"),
                (String) payload.get("password"));
        return ResponseUtil.success(authDto, "Login successful");

    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Map<String, Object> payload) {
        System.out.println(payload);
        User user = authService.register(payload, token);
        return ResponseUtil.created(new AuthDto(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.isActive(),
                token,
                Arrays.asList("")), "Register successful");

    }

    @GetMapping("/reset-password")
    public ResponseEntity<ResponseDto> getResetPasswordLink(@RequestBody Map<String, Object> payload) {
        resetPasswordService.getResetPasswordLink((String) payload.get("email"));
        return ResponseUtil.created(null, "Reset password Email sent successful");
    }

    @PostMapping("/reset-password/{id}")
    public ResponseEntity<ResponseDto> setNewPasswordByResetPasswordEmail(@RequestBody Map<String, Object> payload,
            @PathVariable String id) {
        resetPasswordService.setNewPasswordByResetPasswordEmail(id, (String) payload.get("email"),
                (String) payload.get("new_password"));
        return ResponseUtil.success(null, "Reset password successful");
    }
}
