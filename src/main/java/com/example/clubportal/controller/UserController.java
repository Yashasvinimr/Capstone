package com.example.clubportal.controller;

import java.util.List;
import java.util.Optional;

import com.example.clubportal.dto.UserProfileDTO;
import org.apache.http.HttpStatus;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign the default role of MEMBER
        if (user.getRole() == null) {
            user.setRole(Role.MEMBER);
        } else {
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
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        // {

        // return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid email
        // or password");
        // ss}
        // Role enumRoleValue = user.getRole();
        // Return the user's role (you can return any other user details as needed)
        // return ResponseEntity.ok(user.getRole()); // Return only role or any other
        // details you need
        // return ResponseEntity.ok(enumRoleValue); // Return only role or any other
        // details you need
        // return ResponseEntity.ok(new RoleResponse(user.getRole()));
        return ResponseEntity.ok(user);
    }
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestParam String email,
                                                @RequestBody UserProfileDTO dto) {
        userService.updateProfile(email, dto);
        return ResponseEntity.ok("Profile updated successfully");
    }

    static class RoleResponse {
        private Role role;

        public RoleResponse(Role role) {
            this.role = role;
        }

        public Role getRole() {
            return role;
        }
    }
    

}
