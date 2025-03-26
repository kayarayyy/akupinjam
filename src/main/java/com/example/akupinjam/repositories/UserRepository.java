package com.example.akupinjam.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String name);
    Optional<User> findByEmail(String email);
}
