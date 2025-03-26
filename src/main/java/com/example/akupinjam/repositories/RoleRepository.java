package com.example.akupinjam.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    
    boolean existsByName(String name);

    Optional<Role> findByName(String string);
}
