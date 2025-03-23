package com.example.akupinjam.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.services.RoleService;
import com.example.akupinjam.utils.JwtUtil;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            Integer roleId = jwtUtil.getRoleIdFromToken();

            if (!roleId.equals(1)) {
                return ResponseEntity.status(401).body(new ResponseDto(401, "failed", "Unauthorized request", null));
            }

            ResponseDto responseDto = roleService.getAllRoles();
            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(400, "failed", e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable int id) {
        try {
            Integer roleId = jwtUtil.getRoleIdFromToken();

            if (!roleId.equals(1)) {
                return ResponseEntity.status(401).body(new ResponseDto(401, "failed", "Unauthorized request", null));
            }

            ResponseDto responseDto = roleService.getRoleById(id);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Map<String, Object> payload) {
        try {
            Integer roleId = jwtUtil.getRoleIdFromToken();

            if (!roleId.equals(1)) {
                return ResponseEntity.status(401).body(new ResponseDto(401, "failed", "Unauthorized request", null));
            }

            ResponseDto responseDto = roleService.createRole(payload);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        try {
            Integer roleId = jwtUtil.getRoleIdFromToken();

            if (!roleId.equals(1)) {
                return ResponseEntity.status(401).body(new ResponseDto(401, "failed", "Unauthorized request", null));
            }
            ResponseDto responseDto = roleService.putRole(payload, id);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable int id) {
        try {
            Integer roleId = jwtUtil.getRoleIdFromToken();

            if (!roleId.equals(1)) {
                return ResponseEntity.status(401).body(new ResponseDto(401, "failed", "Unauthorized request", null));
            }
            ResponseDto responseDto = roleService.deleteRole(id);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
