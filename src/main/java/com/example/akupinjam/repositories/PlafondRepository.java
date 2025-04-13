package com.example.akupinjam.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.Plafond;
import com.example.akupinjam.models.enums.Plan;

@Repository
public interface PlafondRepository extends JpaRepository<Plafond, UUID> {
    Optional<Plafond> findByPlan(Plan plan);
    boolean existsByPlan(Plan plan);
}