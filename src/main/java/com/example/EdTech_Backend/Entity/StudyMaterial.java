package com.example.EdTech_Backend.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String filePath;
    private Long size;

    @ManyToOne
    @JoinColumn(name="schoolclass_id")
    private SchoolClass schoolClass;


    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;

}
