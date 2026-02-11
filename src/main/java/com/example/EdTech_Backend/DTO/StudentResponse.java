package com.example.EdTech_Backend.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {

    private Long id;
    private String fullname;
    private String email;
    private String className;
    private String schoolName;


    public StudentResponse(long id, String fullName, String email, String className, String schoolName) {
        this.id=id;
        this.fullname=fullName;
        this.className=className;
        this.schoolName=schoolName;
        this.email=email;
    }
}
