package com.example.EdTech_Backend.Repository;

import com.example.EdTech_Backend.Entity.Role;
import com.example.EdTech_Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    long CountByRole(Role role);



}
