package com.example.akupinjam.controllers;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.models.User;
import com.example.akupinjam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Secured("FEATURE_MANAGE_USERS")
    @GetMapping
    public ResponseEntity<ResponseDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ResponseDto(200, "success", users.size() + " users found", users));
    }

    @Secured({ "FEATURE_MANAGE_USERS", "FEATURE_MANAGE_PROFILE" })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new ResponseDto(200, "success", "User found", user));
    }

    @Secured("FEATURE_MANAGE_USERS")
    @PostMapping
    public ResponseEntity<ResponseDto> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(201, "success", "User created", createdUser));
    }

    @Secured("FEATURE_MANAGE_USERS")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable String id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(new ResponseDto(200, "success", "User updated", updatedUser));
    }
    
    @Secured("FEATURE_MANAGE_USERS")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ResponseDto(200, "success", "User deleted", null));
    }
}
