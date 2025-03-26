package com.example.akupinjam.services;

import com.example.akupinjam.exceptions.ResourceNotFoundException;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public boolean getUserByEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Create a new user
    public User createUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("User already exists");
                });
        Optional<Role> role = roleRepository.findById(user.getRole().getId().toString());

        if (role.isEmpty()) {
            throw new ResourceNotFoundException("Role not found!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Update user
    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setActive(userDetails.isActive());

        Optional<Role> role = roleRepository.findById(userDetails.getRole().getId().toString());
        if (role.isEmpty()) {
            throw new ResourceNotFoundException("Role not found!");
        }
        user.setRole(role.get());

        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        userRepository.deleteById(id);
    }
}
