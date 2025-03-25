package com.example.akupinjam.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.ResetPassword;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, UUID> {
    
}
