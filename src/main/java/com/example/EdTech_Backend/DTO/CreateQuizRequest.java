package com.example.EdTech_Backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateQuizRequest {

    private String className;
    private String question;
    private List<String> options;
    private Integer correctIndex;
}
