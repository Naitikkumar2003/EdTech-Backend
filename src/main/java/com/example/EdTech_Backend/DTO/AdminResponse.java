package com.example.EdTech_Backend.DTO;


import com.example.EdTech_Backend.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminResponse {
    private Long id;
    private String email;
    private String role;
    private LocalDateTime created_at;

}
