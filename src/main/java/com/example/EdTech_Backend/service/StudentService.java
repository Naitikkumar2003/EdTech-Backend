package com.example.EdTech_Backend.service;


import com.example.EdTech_Backend.DTO.StudentResponse;
import com.example.EdTech_Backend.DTO.UpdateStudentRequest;
import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.StudentRepository;
import com.example.EdTech_Backend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;


    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }


    public List<StudentResponse> getAllStudents(){
        List<Student> students=studentRepository.findAll();

        return students.stream()
                .map(student -> new StudentResponse(
               student.getId(),
                student.getFullName(),
                student.getUser().getEmail(),
                student.getClassName(),
                student.getSchoolName()
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

}
