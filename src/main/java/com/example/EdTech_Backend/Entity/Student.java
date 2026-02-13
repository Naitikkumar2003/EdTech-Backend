package com.example.EdTech_Backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    private String className;
    private  String schoolName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "father_phone")
    private String fatherPhone;

    @Column(name = "home_address", columnDefinition = "TEXT")
    private String homeAddress;


}
