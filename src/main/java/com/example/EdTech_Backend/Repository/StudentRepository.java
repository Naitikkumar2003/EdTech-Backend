package com.example.EdTech_Backend.Repository;

import com.example.EdTech_Backend.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {

}
