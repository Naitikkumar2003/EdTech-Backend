package com.example.EdTech_Backend.service;

import com.example.EdTech_Backend.DTO.AdminResponse;
import com.example.EdTech_Backend.Entity.Role;
import com.example.EdTech_Backend.Entity.Student;
import com.example.EdTech_Backend.Entity.StudyMaterial;
import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.StudentRepository;
import com.example.EdTech_Backend.Repository.StudyMaterialRepository;
import com.example.EdTech_Backend.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudyMaterialRepository studyMaterialRepository;

    public CustomUserDetailsService(UserRepository userRepository, StudentRepository studentRepository, StudyMaterialRepository studyMaterialRepository){
        this.userRepository=userRepository;
        this.studentRepository = studentRepository;
        this.studyMaterialRepository = studyMaterialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        System.out.println("User loaded: " + user.getEmail());
        System.out.println("DB password: " + user.getPassword());
        System.out.println("User role :"+user.getRole());


        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())   // Automatically adds ROLE_
                .build();
    }



    /*public void deleteAdmin(Long id){
        User u
    }*/
    public List<AdminResponse> getAllAdmina(){
        List<User> admins=userRepository.findByRole(Role.ADMIN);

        return admins.stream()
                .map(user -> new AdminResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()
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

}
