package com.example.akupinjam.models;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@Table(name = "roles")

public class Role {
    @Id
    @Column(length = 10)
    private String id;

    private String generateRandomId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public Role() {
        this.id = generateRandomId();
    }

    @Column(nullable = false, unique = true)
    private String name;
}
