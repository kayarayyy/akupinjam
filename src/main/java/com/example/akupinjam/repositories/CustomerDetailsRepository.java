package com.example.akupinjam.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.CustomerDetails;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, UUID> {
    Optional<CustomerDetails> findByUserEmail(String email);
}

