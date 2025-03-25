package com.example.akupinjam.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.akupinjam.models.Role;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.exceptions.ResourceNotFoundException;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with ID " + id + " not found!"));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role with ID " + name + " not found!"));
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(String id, Role updatedRole) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with ID " + id + " not found!"));

        role.setName(updatedRole.getName());
        return roleRepository.save(role);
    }

    public void deleteRole(String id) {
        roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with ID " + id + " not found!"));
        
        roleRepository.deleteById(id);
    }
}
