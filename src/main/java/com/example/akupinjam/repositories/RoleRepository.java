package com.example.akupinjam.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);

    Optional<Role> findByName(String string);
}
