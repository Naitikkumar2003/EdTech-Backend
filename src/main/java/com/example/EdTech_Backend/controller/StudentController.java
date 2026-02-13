package com.example.EdTech_Backend.controller;

import com.example.EdTech_Backend.DTO.UpdateStudentRequest;
import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.StudyMaterial;
import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.QuizRepository;
import com.example.EdTech_Backend.Repository.StudentRepository;
import com.example.EdTech_Backend.Repository.StudyMaterialRepository;
import com.example.EdTech_Backend.Repository.UserRepository;
import com.example.EdTech_Backend.service.MaterialService;
import com.example.EdTech_Backend.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final QuizRepository quizRepository;
    private final MaterialService materialService;

    @GetMapping("/test")
    public String studentTest() {
        return "Student Access Granted";
    }

    //getb classs subject
   // @GetMapping("/class/{classid}/subjects")


    // get material by subjectcode

    @GetMapping("/materials")
    public ResponseEntity<?> getMaterials() {
        return ResponseEntity.ok(studentService.getMaterialsForLoggedStudent());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {

        return ResponseEntity.ok(studentService.getLoggedStudentDetails());
    }

    @GetMapping("/quizzes")
    public ResponseEntity<?> getStudentQuizzes(Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(studentService.getStudentQuizzes(email));
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> getSingleQuiz(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(quizRepository.findById(id));
    }
    @PostMapping("/student/profile-update")
    public ResponseEntity<String> updateProfile(
            @RequestBody UpdateStudentRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        studentService.updateProfile(email, request);

        return ResponseEntity.ok("Profile updated successfully");
    }

    @GetMapping("/download/{id}")
    private ResponseEntity<?> downloadmaterial(@PathVariable Long id)throws Exception{
        return ResponseEntity.ok(materialService.downloadMaterial(id));

    }








}
