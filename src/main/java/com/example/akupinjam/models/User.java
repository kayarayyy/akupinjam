package com.example.akupinjam.models;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(length = 10)
    private String id;

    private String generateRandomId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public User() {
        this.id = generateRandomId();
    }

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Column(nullable = false)
    private boolean isActive;
}

