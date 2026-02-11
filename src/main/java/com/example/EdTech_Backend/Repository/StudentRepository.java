package com.example.EdTech_Backend.Repository;

import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
     List<Student> findByFullNameContainingIgnoreCase(String  fullname);
     Optional<Student> findByUser(User user);


}
