package com.example.akupinjam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.Feature;


@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    boolean existsByName(String name);
}
