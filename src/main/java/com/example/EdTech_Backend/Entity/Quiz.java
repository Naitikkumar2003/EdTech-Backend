package com.example.EdTech_Backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.example.EdTech_Backend.Entity.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;

    private String question;

    private Long correctOptionId;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();
}
