package com.example.EdTech_Backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateStudentRequest {
    private String fatherName;
    private String fatherPhone;
    private String homeAddress;

}
