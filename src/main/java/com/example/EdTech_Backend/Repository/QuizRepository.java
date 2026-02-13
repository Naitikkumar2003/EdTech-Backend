package com.example.EdTech_Backend.Repository;

import com.example.EdTech_Backend.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findByClassName(String className);

}
