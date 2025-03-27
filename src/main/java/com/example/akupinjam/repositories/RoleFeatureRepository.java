package com.example.akupinjam.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.RoleFeature;

@Repository
public interface RoleFeatureRepository extends JpaRepository<RoleFeature, UUID> {
}