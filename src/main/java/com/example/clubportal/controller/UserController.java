package com.example.clubportal.controller;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clubportal.dto.LoginRequest;
import com.example.clubportal.entity.ClubMember.Role;
import com.example.clubportal.entity.User;
import com.example.clubportal.exceptions.UserNotFoundException;
import com.example.clubportal.repository.UserRepository;
import com.example.clubportal.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{10}$"; // Validating 10-digit phone numbers
        return phoneNumber.matches(phoneRegex);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Invalid phone number format");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign the default role of MEMBER
        if (user.getRole() ==null){
            user.setRole(Role.MEMBER);
        }
        else{
            user.setRole(user.getRole());
        }
        

        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequest.getEmail()));

        // Verify password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid email or password");
        }

        // Return the user's role (you can return any other user details as needed)
        return ResponseEntity.ok(user.getRole()); // Return only role or any other details you need
    }
}
