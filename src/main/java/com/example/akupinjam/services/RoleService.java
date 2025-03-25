package com.example.akupinjam.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public ResponseDto getAllRoles() {
        try {

            List<Role> role = roleRepository.findAll();

            return new ResponseDto(200, "success", role.isEmpty() ? "Record not found" : role.size() + " records found",
                    role);
        } catch (RuntimeException e) {
            return new ResponseDto(501, "failed", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseDto(400, "failed", "Something wrong: " + e.getMessage(), null);
        }
    }

    public ResponseDto getRoleById(String id) {
        try {
            Optional<Role> role = roleRepository.findById(id);

            return new ResponseDto(200, "success", role.isEmpty() ? "Record not found" : "Record found", role);
        } catch (RuntimeException e) {
            return new ResponseDto(501, "failed", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseDto(400, "failed", "Something wrong: " + e.getMessage(), null);
        }
    }

    public ResponseDto createRole(Map<String, Object> payload) {
        try {
            Role role = new Role();
            role.setName((String) payload.get("name"));

            role = roleRepository.save(role);

            return new ResponseDto(200, "success", "Role saved!", role);
        } catch (RuntimeException e) {
            return new ResponseDto(501, "failed", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseDto(400, "failed", "Something wrong: " + e.getMessage(), null);
        }
    }

    public ResponseDto deleteRole(String id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Record not found!"));

            roleRepository.deleteById(id);
            return new ResponseDto(200, "success", "Record deleted!", role);

        } catch (RuntimeException e) {
            return new ResponseDto(501, "failed", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseDto(400, "failed", "Something went wrong: " + e.getMessage(), null);
        }
    }

    public ResponseDto putRole(Map<String, Object> payload, String id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Record not found!"));

            role.setName((String) payload.get("name"));
            roleRepository.save(role);

            return new ResponseDto(200, "success", "Role updated!", role);
        } catch (RuntimeException e) {
            return new ResponseDto(501, "failed", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseDto(400, "failed", "Something wrong: " + e.getMessage(), null);
        }
    }
}
