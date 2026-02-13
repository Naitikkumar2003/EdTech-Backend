package com.example.EdTech_Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendUserCredentials(String toEmail, String password, String role) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to CBSE AI EdTech 🎓");

        message.setText(
                "Your " + role + " account has been created successfully.\n\n" +
                        "Email: " + toEmail + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please login and change your password immediately."
        );

        mailSender.send(message);
    }


    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP - CBSE AI EdTech");

        message.setText(
                "Your OTP for password reset is:\n\n" +
                        otp +
                        "\n\nThis OTP will expire in 10 minutes."
        );

        mailSender.send(message);
    }



}
