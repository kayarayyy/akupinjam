package com.example.akupinjam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.akupinjam.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
