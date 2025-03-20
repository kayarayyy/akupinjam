package com.example.akupinjam.controller;

import com.example.akupinjam.models.User;
import com.example.akupinjam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET: Mendapatkan semua user
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // POST: Menambahkan user baru
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // GET: Mendapatkan user berdasarkan ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // PUT: Mengupdate data user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setNip(updatedUser.getNip());
            user.setRole(updatedUser.getRole());
            user.setAlamat(updatedUser.getAlamat());
            return userRepository.save(user);
        }).orElse(null);
    }

    // DELETE: Menghapus user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
