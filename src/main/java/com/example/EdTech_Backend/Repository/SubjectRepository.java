package com.example.EdTech_Backend.Repository;

import com.example.EdTech_Backend.Entity.SchoolClass;
import com.example.EdTech_Backend.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    List<Subject> findBySchoolClass(SchoolClass schoolClass);
    List<Subject> findBySchoolClass_Id(Long classId);



}
