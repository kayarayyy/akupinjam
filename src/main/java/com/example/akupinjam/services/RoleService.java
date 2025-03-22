package com.example.akupinjam.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.utils.JwtUtil;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseDto getAllRoles() {
        try {
            Integer role_id = jwtUtil.getRoleIdFromToken();

            if (role_id != 1) {

                return new ResponseDto(401, "failed", "Unauthorized request",
                        null);
            }

            List<Role> role = roleRepository.findAll();

            return new ResponseDto(200, "success", role.isEmpty() ? "Record not found" : role.size() + " records found",
                    role);
        } catch (Exception e) {
            return new ResponseDto(400, "error", e.getMessage(), null);
        }
    }

    public ResponseDto getRoleById(int id) {
        try {
            Optional<Role> role = roleRepository.findById(id);

            return new ResponseDto(200, "success", role.isEmpty() ? "Record not found" : "Record found", role);
        } catch (Exception e) {
            return new ResponseDto(400, "error", e.getMessage(), null);
        }
    }

    public ResponseDto createRole(Map<String, Object> payload) {
        try {
            Role role = new Role();
            role.setName((String) payload.get("name"));

            role = roleRepository.save(role);

            return new ResponseDto(200, "success", "Role saved", role);
        } catch (Exception e) {
            return new ResponseDto(400, "error", e.getMessage(), null);
        }
    }

    public ResponseDto deleteRole(int id) {
        try {
            Optional<Role> role = roleRepository.findById(id);

            if (role.isPresent()) {
                roleRepository.deleteById(id);
            }

            return new ResponseDto(200, "success", role.isEmpty() ? "Record not found" : "Record deleted", role);
        } catch (Exception e) {
            return new ResponseDto(400, "error", e.getMessage(), null);
        }
    }
}
