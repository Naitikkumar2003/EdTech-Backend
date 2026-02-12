package com.example.EdTech_Backend.Repository;

import com.example.EdTech_Backend.Entity.SchoolClass;
import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial,Long> {
    List<StudyMaterial> findBySchoolClass_Name(String name);
    List<StudyMaterial> findBySchoolClass_Id(Long classId);

    List<StudyMaterial> findBySubject_Id(Long subjectId);

    List<StudyMaterial> findBySchoolClass_IdAndSubject_Id(Long classId, Long subjectId);

}
