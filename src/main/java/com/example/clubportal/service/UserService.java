package com.example.clubportal.service;

import com.example.clubportal.dto.UserProfileDTO;
import com.example.clubportal.entity.User;
import com.example.clubportal.exceptions.InvalidEmailException;
import com.example.clubportal.exceptions.ResourceNotFoundException;
import com.example.clubportal.exceptions.UserNotFoundException;
import com.example.clubportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }
    

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    public User registerUser(User user) {
        // Validating the email format for the specified domain
        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@bmsce\\.ac\\.in$")) {
            throw new InvalidEmailException("Invalid email format. Only @bmsce.ac.in allowed.");
        }
        return userRepository.save(user);
    }

    public List<User> findUsersByClubAndName(Long clubId, String query) {
        return userRepository.findUsersByClubAndName(clubId, query);
    }

    public UserProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserProfileDTO(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getYear(),
                user.getDepartment()
        );
    }

    public void updateProfile(String email, UserProfileDTO updated) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(updated.getName());
        user.setPhoneNumber(updated.getPhoneNumber());
        user.setYear(updated.getYear());
        userRepository.save(user);
    }

}
