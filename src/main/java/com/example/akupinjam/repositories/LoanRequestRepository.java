package com.example.akupinjam.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.LoanRequest;

import jakarta.persistence.LockModeType;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.id = :id")
    Optional<LoanRequest> findWithLockById(@Param("id") UUID id);

}
