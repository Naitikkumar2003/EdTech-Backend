package com.example.EdTech_Backend.service;

import com.example.EdTech_Backend.DTO.AdminResponse;
import com.example.EdTech_Backend.Entity.Role;
import com.example.EdTech_Backend.Entity.User;
import com.example.EdTech_Backend.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        System.out.println("User loaded: " + user.getEmail());
        System.out.println("DB password: " + user.getPassword());


        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())   // Automatically adds ROLE_
                .build();
    }



    /*public void deleteAdmin(Long id){
        User u
    }*/
    public List<AdminResponse> getAllAdmina(){
        List<User> admins=userRepository.findByRole(Role.ADMIN);

        return admins.stream()
                .map(user -> new AdminResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();
    }
    public void deleteAdmin(Long id){
        Optional<User> user=userRepository.findById(id);
        userRepository.delete(user);

    }

}
