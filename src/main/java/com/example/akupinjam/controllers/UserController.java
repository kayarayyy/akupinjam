package com.example.akupinjam.controllers;

import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (roleRepository.existsById(user.getRole().getId())) {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.badRequest().build();
    }

    // Update user
    // @PutMapping("/{id}")
    // public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User userDetails) {
    //     Optional<User> optionalUser = userRepository.findById(id);

    //     if (optionalUser.isPresent()) {
    //         User user = optionalUser.get();
    //         user.setName(userDetails.getName());
    //         user.setEmail(userDetails.getEmail());
    //         user.setPassword(userDetails.getPassword());
    //         user.setNip(userDetails.getNip());
    //         user.setAlamat(userDetails.getAlamat());

    //         if (roleRepository.existsById(userDetails.getRole().getId())) {
    //             user.setRole(userDetails.getRole());
    //             userRepository.save(user);
    //             return ResponseEntity.ok(user);
    //         } else {
    //             return ResponseEntity.badRequest().build();
    //         }
    //     }

    //     return ResponseEntity.notFound().build();
    // }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
