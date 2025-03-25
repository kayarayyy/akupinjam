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
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {

            ResponseDto responseDto = roleService.getAllRoles();
            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(400, "failed", e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById( @PathVariable String id) {
        try {
            ResponseDto responseDto = roleService.getRoleById(id);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Map<String, Object> payload) {
        try {
            ResponseDto responseDto = roleService.createRole(payload);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole( @PathVariable String id, @RequestBody Map<String, Object> payload) {
        try {
            ResponseDto responseDto = roleService.putRole(payload, id);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole( @PathVariable String id) {
        try {
            ResponseDto responseDto = roleService.deleteRole(id);

            return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
