package com.example.EdTech_Backend.service;

import com.example.EdTech_Backend.DTO.AdminResponse;
import com.example.EdTech_Backend.DTO.CreateStudentRequest;
import com.example.EdTech_Backend.DTO.RegisterRequest;
import com.example.EdTech_Backend.DTO.CreateQuizRequest;
import com.example.EdTech_Backend.Entity.*;
import com.example.EdTech_Backend.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudyMaterialRepository studyMaterialRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final QuizRepository quizRepository;
    private final OptionRepository optionRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;





    public String createStudent(CreateStudentRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setClassName(request.getClassName());
        student.setSchoolName(request.getSchoolName());
        student.setUser(user);
        studentRepository.save(student);

        // 🔥 ADD THIS LINE ONLY
        emailService.sendUserCredentials(
                request.getEmail(),
                request.getPassword(),
                "STUDENT"
        );


        return "Student created successfully";
    }


    public String createAdmin(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        emailService.sendUserCredentials(
                request.getEmail(),
                request.getPassword(),
                "ADMIN"
        );


        return "Admin created successfully";
    }

    public List<Student> searchStudent(String name){
        return studentRepository.findByFullNameContainingIgnoreCase(name);
    }
    public String deleteMaterial(Long id) {

        StudyMaterial material = studyMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        File file = new File(material.getFilePath());
        if (file.exists()) {
            file.delete();
        }
        studyMaterialRepository.delete(material);

        return "Study material deleted successfully";
    }
    public List<AdminResponse> getAllAdmina(){
        List<User> admins=userRepository.findByRole(Role.ADMIN);

        return admins.stream()
                .map(user -> new AdminResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getCreatedAt()
                ))
                .toList();
    }




    public String deleteAdmin(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not Found"));

        if (user.getRole()!=Role.ADMIN){
            throw new RuntimeException("User is not Admin");

        }
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        String loggedinmail= authentication.getName();
        if (user.getEmail().equals(loggedinmail)){
            throw new RuntimeException("You cannot delete yourself");
        }
        userRepository.delete(user);
        return "Admin delete Sucessfully";
    }
    public List<Subject> searchsubject(Long classId){

        return subjectRepository.findBySchoolClass_Id(classId);

    }

    public String uploadfile(MultipartFile file,Long subjectId,Long classId) throws IOException {


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
        return "file uploaded";
    }

    public String addsubject(Long classId,Subject subject){
        SchoolClass schoolClass=schoolClassRepository.findById(classId)
                .orElseThrow(()->new RuntimeException("Class Not Found"));


        subject.setSchoolClass(schoolClass);
        subjectRepository.save(subject);
        return "subject is added";
    }
    public List<StudyMaterial> getMaterialsByClassAndSubject(Long classId, Long subjectId) {

        if (!schoolClassRepository.existsById(classId)) {
            throw new RuntimeException("Class not found");
        }

        if (!subjectRepository.existsById(subjectId)) {
            throw new RuntimeException("Subject not found");
        }

        return studyMaterialRepository
                .findBySchoolClass_IdAndSubject_Id(classId, subjectId);
    }



    public void createQuiz(CreateQuizRequest request) {

        Quiz quiz = new Quiz();
        quiz.setClassName(request.getClassName());
        quiz.setQuestion(request.getQuestion());
        quiz.setCreatedAt(LocalDateTime.now());

        List<Option> optionList = new ArrayList<>();

        for (int i = 0; i < request.getOptions().size(); i++) {

            Option option = new Option();
            option.setText(request.getOptions().get(i));
            option.setQuiz(quiz);

            optionList.add(option);

            if (i == request.getCorrectIndex()) {
                // We will set after saving
            }
        }

        quiz.setOptions(optionList);

        quiz = quizRepository.save(quiz);

        // Now set correctOptionId
        Option correctOption = quiz.getOptions().get(request.getCorrectIndex());
        quiz.setCorrectOptionId(correctOption.getId());

        quizRepository.save(quiz);
    }


    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }



}
