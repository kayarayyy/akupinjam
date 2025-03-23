package com.example.akupinjam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.LoanRequest;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, String> {
    
}
