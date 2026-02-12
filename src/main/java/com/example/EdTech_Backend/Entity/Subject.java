package com.example.EdTech_Backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
    private String name;


    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

}
