package com.example.EdTech_Backend.service;


import com.example.EdTech_Backend.DTO.ResetPasswordRequest;
import com.example.EdTech_Backend.Entity.User;

import com.example.EdTech_Backend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;






    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        System.out.println("User loaded: " + user.getEmail());
        System.out.println("DB password: " + user.getPassword());
        System.out.println("User role :"+user.getRole());


        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())   // Automatically adds ROLE_
                .build();
    }



    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Generate 6 digit OTP
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setResetOtp(otp);
        user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(10));


        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    public void resetPasswordWithOtp(String email, String otp, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));



        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");

        }

        if (user.getResetOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));



        // Clear OTP
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);

        userRepository.save(user);
    }




}
