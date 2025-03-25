package com.example.akupinjam.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authServices;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        try {
            ResponseDto responseDto = authServices.login(payload);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody Map<String, Object> payload) {
        try {
            System.out.println(token);
            
            ResponseDto responseDto = authServices.register(payload, token);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
