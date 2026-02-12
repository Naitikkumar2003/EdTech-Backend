package com.example.EdTech_Backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private String fullname;
    private String email;
    private String className;
    private String schoolName;
    private LocalDateTime localDateTime;



}
