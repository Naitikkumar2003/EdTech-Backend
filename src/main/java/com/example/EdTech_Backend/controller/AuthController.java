package com.example.EdTech_Backend.controller;


import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.UserRepository;
import com.example.EdTech_Backend.DTO.JwtResponse;
import com.example.EdTech_Backend.DTO.LoginRequest;
import com.example.EdTech_Backend.service.CustomUserDetailsService;
import com.example.EdTech_Backend.service.JwtService;
import com.example.EdTech_Backend.DTO.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        Authentication authentication=authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        System.out.println("Trying login for: " + request.getEmail());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        System.out.println("Email: " + request.getEmail());
        System.out.println("Role: " + request.getRole());
        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
//        userRepository.save(user);
        return ResponseEntity.ok("Registered Succedfully");

    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(customUserDetailsService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        return ResponseEntity.ok(customUserDetailsService.resetPassword(token, newPassword));
    }
}
