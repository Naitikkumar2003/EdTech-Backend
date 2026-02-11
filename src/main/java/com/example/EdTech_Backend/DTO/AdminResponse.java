package com.example.EdTech_Backend.DTO;


import com.example.EdTech_Backend.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminResponse {
    private Long id;
    private String email;
    private String name;
    private Role role;

    public AdminResponse(Long id, String email, String name) {
        this.id=id;
        this.email=email;
        this.name=name;
    }
}
