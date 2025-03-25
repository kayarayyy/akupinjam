package com.example.akupinjam.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.services.RoleService;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<ResponseDto> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(new ResponseDto(200, "success", roles.size() + " roles found", roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getRoleById(@PathVariable String id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(new ResponseDto(200, "success", "Role found", role));
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto(201, "success", "Role created", createdRole));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateRole(@PathVariable String id, @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(id, role);
        return ResponseEntity.ok(new ResponseDto(200, "success", "Role updated", updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new ResponseDto(200, "success", "Role deleted", null));
    }
}
