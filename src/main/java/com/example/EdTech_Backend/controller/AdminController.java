package com.example.EdTech_Backend.controller;

import com.example.EdTech_Backend.DTO.AdminResponse;
import com.example.EdTech_Backend.DTO.CreateStudentRequest;
import com.example.EdTech_Backend.Entity.*;
import com.example.EdTech_Backend.Repository.*;
import com.example.EdTech_Backend.DTO.RegisterRequest;
import com.example.EdTech_Backend.service.AdminService;
import com.example.EdTech_Backend.service.CustomUserDetailsService;
import com.example.EdTech_Backend.service.MaterialService;
import com.example.EdTech_Backend.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
   private final UserRepository userRepository;
   private final StudentRepository studentRepository;
   private final PasswordEncoder passwordEncoder;
   private final SchoolClassRepository schoolClassRepository;
   private final SubjectRepository subjectRepository;
   private final StudyMaterialRepository studyMaterialRepository;
   private final StudentService studentService;
   private final CustomUserDetailsService customUserDetailsService;
   private final AdminService adminService;
   private final MaterialService materialService;



    @GetMapping("/test")
    public String test(){
        return "Admin working";
    }


    @PostMapping("/create_student")
    public ResponseEntity<?> create_user(@RequestBody CreateStudentRequest request){
        return ResponseEntity.ok(adminService.createStudent(request));
   }


   @PostMapping("/create-admin")
    public ResponseEntity<?> createadmin(@RequestBody RegisterRequest request){
       return ResponseEntity.ok(adminService.createAdmin(request));
   }


   // it can add classs but not required
   @PostMapping("/class")
    public ResponseEntity<?> addclass(@RequestBody SchoolClass schoolClass){
        schoolClassRepository.save(schoolClass);
        return ResponseEntity.ok("class added");

   }


   // it add the subject
    @PostMapping("/addsubject")
    public ResponseEntity<?> addsubject(@RequestParam Long classId,@RequestBody Subject subject){
        return ResponseEntity.ok(adminService.addsubject(classId,subject));
    }

    // it upload the pdf or material
    @PostMapping(value="/upload",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam("classId") Long classId
    )throws IOException {
        return ResponseEntity.ok(adminService.uploadfile(file,subjectId,classId));
    }



    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }


    @DeleteMapping("/student/{id}")
    public ResponseEntity<?>  deleteStudent(@PathVariable  Long id){
        studentService.deleterStudent(id);
        return ResponseEntity.ok("Student deleted Sucessfully");

    }


    @GetMapping("/get-admin")
    public ResponseEntity<?>  getAllAdmins(){
        List<AdminResponse> admins=adminService.getAllAdmina();
        return ResponseEntity.ok(admins);
    }


    @DeleteMapping("/admin/delete/{id}")
    public  ResponseEntity<?> deleteAdmin(@PathVariable Long id){

        String respose=adminService.deleteAdmin(id);
        return ResponseEntity.ok(respose);
    }


    @GetMapping("/student/search")
    public ResponseEntity<?> searchtudents(@RequestParam String name){
        return ResponseEntity.ok(adminService.searchStudent(name));
    }


    @GetMapping("/getsubjects")
    public ResponseEntity<?> searchsubjectbyclass(@RequestParam Long classId){
        return ResponseEntity.ok(adminService.searchsubject(classId));
    }


    @DeleteMapping("/deletematerial/{id}")
    public ResponseEntity<?> deletematerial(@PathVariable("id") Long id){
        return ResponseEntity.ok(adminService.deleteMaterial(id));
    }

    @GetMapping("/materials/class/{classId}")
    public ResponseEntity<?> getMaterialsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(materialService.getMaterialsByClass(classId));
    }
    @GetMapping("/materials/class/{classId}/subject/{subjectId}")
    public ResponseEntity<?> getMaterials(
            @PathVariable Long classId,
            @PathVariable Long subjectId
    ) {
        return ResponseEntity.ok(
                adminService.getMaterialsByClassAndSubject(classId, subjectId)
        );
    }



}
