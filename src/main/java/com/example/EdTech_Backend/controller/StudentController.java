package com.example.EdTech_Backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/test")
    public String studentTest() {
        return "Student Access Granted";
    }

    //getb classs subject
   // @GetMapping("/class/{classid}/subjects")


    // get material by subjectcode
    //@GetMapping("/subject/{subjectid}/materials")

    //download material by id
   // @GetMapping("/download/{materialid}")


}
