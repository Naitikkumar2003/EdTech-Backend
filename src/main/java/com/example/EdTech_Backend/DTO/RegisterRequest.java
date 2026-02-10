package com.example.EdTech_Backend.DTO;


import com.example.EdTech_Backend.Entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    private String email;
    private String password;
    private Role role;
}
