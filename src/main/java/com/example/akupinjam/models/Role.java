package com.example.akupinjam.models;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

}
