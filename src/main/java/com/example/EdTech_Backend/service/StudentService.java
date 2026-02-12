package com.example.EdTech_Backend.service;


import com.example.EdTech_Backend.DTO.ResetPasswordRequest;
import com.example.EdTech_Backend.DTO.StudentResponse;
import com.example.EdTech_Backend.DTO.UpdateStudentRequest;
import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.StudyMaterial;
import com.example.EdTech_Backend.Entity.Subject;
import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.StudentRepository;
import com.example.EdTech_Backend.Repository.StudyMaterialRepository;
import com.example.EdTech_Backend.Repository.SubjectRepository;
import com.example.EdTech_Backend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final StudyMaterialRepository studyMaterialRepository;


    public List<StudentResponse> getAllStudents(){
        List<Student> students=studentRepository.findAll();

        return students.stream()
                .map(student -> new StudentResponse(
               student.getId(),
                student.getFullName(),
                student.getUser().getEmail(),
                student.getClassName(),
                student.getSchoolName(),
                student.getUser().getCreatedAt()
        ))
                .collect(Collectors.toList());
    }

    public void deleterStudent(Long id){

        Student student=studentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        User user=student.getUser();

        studentRepository.delete(student);
        userRepository.delete(user);
    }
    public List<StudyMaterial> getMaterialsForLoggedStudent() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String className = student.getClassName();

        return studyMaterialRepository.findBySchoolClass_Name(className);
    }
    public Student getLoggedStudentDetails() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }








}
