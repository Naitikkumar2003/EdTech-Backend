package com.example.EdTech_Backend.controller;

import com.example.EdTech_Backend.DTO.AdminResponse;
import com.example.EdTech_Backend.DTO.CreateStudentRequest;
import com.example.EdTech_Backend.Entity.*;
import com.example.EdTech_Backend.Repository.*;
import com.example.EdTech_Backend.DTO.RegisterRequest;
import com.example.EdTech_Backend.service.CustomUserDetailsService;
import com.example.EdTech_Backend.service.StudentService;
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
public class AdminController {
   private final UserRepository userRepository;
   private final StudentRepository studentRepository;
   private final PasswordEncoder passwordEncoder;
   private final SchoolClassRepository schoolClassRepository;
   private final SubjectRepository subjectRepository;
   private final StudyMaterialRepository studyMaterialRepository;
   private final StudentService studentService;
   private final CustomUserDetailsService customUserDetailsService;

    public AdminController(UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository, StudyMaterialRepository studyMaterialRepository, StudentService studentService, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;

        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.studyMaterialRepository = studyMaterialRepository;
        this.studentService = studentService;
        this.customUserDetailsService = customUserDetailsService;
    }
    @GetMapping("/test")
    public String test(){
        return "Admin working";
    }


    @PostMapping("/create_student")
    public ResponseEntity<?> create_user(@RequestBody CreateStudentRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
       userRepository.save(user);


        Student student=new Student();

        student.setClassName(request.getClassName());
        student.setFullName(request.getFullName());
        student.setSchoolName(request.getSchoolName());
        student.setUser(user);
        studentRepository.save(student);


        return ResponseEntity.ok("Student createc by admin");
   }


   @PostMapping("/create-admin")
    public ResponseEntity<?> createadmin(@RequestBody RegisterRequest request){
        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
       if(userRepository.findByEmail(request.getEmail()).isPresent()){
           return ResponseEntity.badRequest().body("Email already exists");
       }

       userRepository.save(user);
        return ResponseEntity.ok("Admin is added by Admin");
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

        SchoolClass schoolClass=schoolClassRepository.findById(classId)
                        .orElseThrow(()->new RuntimeException("Class Not Found"));


        subject.setSchoolClass(schoolClass);
        subjectRepository.save(subject);
        return ResponseEntity.ok("Subject added");

    }

    // it upload the pdf or material
    @PostMapping(value="/upload",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam("classId") Long classId
    ) throws IOException {

        SchoolClass schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        String uploaddir=System.getProperty("user.dir") + "/uploads/";
        File directory=new File(uploaddir);
        if(!directory.exists()){
            directory.mkdirs();
        }

        String filepath=uploaddir + file.getOriginalFilename();
        file.transferTo(new File(filepath));

        StudyMaterial material=new StudyMaterial();
        material.setFilePath(filepath);
        material.setFilename(file.getOriginalFilename());
        material.setSize(file.getSize());
        material.setSchoolClass(schoolClass);
        material.setSubject(subject);


        studyMaterialRepository.save(material);
        return ResponseEntity.ok("Study Material added");
    }




    /// Get MApping start from here
    ///


    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }


    @DeleteMapping("/student/{id}")
    public ResponseEntity<?>  deleteStudent(@PathVariable  Long id){
        studentService.deleterStudent(id);
        return ResponseEntity.ok("Student deleted Sucessfully");

    }

    @GetMapping("/admin")
    public ResponseEntity<?>  getAllAdmins(){
        List<AdminResponse> admins=customUserDetailsService.getAllAdmina();
        return ResponseEntity.ok(admins);
    }
    @DeleteMapping("/admin/delete/{id}")
    public  ResponseEntity<?> deleteAdmin(@PathVariable Long id){

        String respose= customUserDetailsService.deleteAdmin(id);
        return ResponseEntity.ok(respose);
    }
    @GetMapping("/student/search")
    public ResponseEntity<?> searchtudents(@RequestParam String name){
        return ResponseEntity.ok(customUserDetailsService.searchStudent(name));
    }
    @GetMapping("/getsubjects")
    public ResponseEntity<?> searchsubjectbyclass(@RequestParam Long classId){

        SchoolClass schoolClass= schoolClassRepository.findById(classId)
                .orElseThrow(()-> new RuntimeException("Class not found"));

        List<Subject> subjects=subjectRepository.findBySchoolClass(schoolClass);

        return ResponseEntity.ok(subjects);
    }

    @DeleteMapping("/deletematerial/{id}")
    public ResponseEntity<?> deletematerial(@PathVariable("id") Long id){
        return ResponseEntity.ok(customUserDetailsService.deleteMaterial(id));
    }

}
