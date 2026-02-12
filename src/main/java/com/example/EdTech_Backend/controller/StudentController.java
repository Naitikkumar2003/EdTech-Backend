package com.example.EdTech_Backend.controller;

import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.StudyMaterial;
import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.StudentRepository;
import com.example.EdTech_Backend.Repository.StudyMaterialRepository;
import com.example.EdTech_Backend.Repository.UserRepository;
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

    private final StudyMaterialRepository studyMaterialRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentService studentService;

    @GetMapping("/test")
    public String studentTest() {
        return "Student Access Granted";
    }

    //getb classs subject
   // @GetMapping("/class/{classid}/subjects")


    // get material by subjectcode
    //@GetMapping("/subject/{subjectid}/materials")

    //download material by id
   // @GetMapping("/download/{materialid}")

    /*@GetMapping("/materialss")
    public ResponseEntity<?> getMaterial(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("USer not Found"));

        Student student=studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String school_class=student.getClassName();
        List<StudyMaterial> materials=studyMaterialRepository.findBySchoolClass_Name(school_class);
        return ResponseEntity.ok(materials);
    }*/

    @GetMapping("/download/{id}")
    private ResponseEntity<?> downloadmaterial(@PathVariable Long id)throws Exception{
        StudyMaterial studyMaterial=studyMaterialRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Material not available"));

        File file =new File(studyMaterial.getFilePath());
        if(!file.exists()) {
            throw  new RuntimeException("file not found");
        }
        InputStreamResource resource=new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment :filename="
                        +studyMaterial.getFilename())
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }
    @GetMapping("/materials")
    public ResponseEntity<?> getMaterials() {
        return ResponseEntity.ok(studentService.getMaterialsForLoggedStudent());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok(studentService.getLoggedStudentDetails());
    }








}
