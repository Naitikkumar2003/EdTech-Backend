package com.example.EdTech_Backend.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequest {
    private String fullName;
    private String schoolName;
    private String email;
    private String password;
    private String className;

}
