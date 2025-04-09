package com.example.clubportal.service;

import com.example.clubportal.dto.ClubDTO;
import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.ClubMember;
import com.example.clubportal.entity.User;
import com.example.clubportal.exceptions.ClubNotFoundException;
import com.example.clubportal.exceptions.UserNotFoundException;
import com.example.clubportal.repository.ClubMemberRepository;
import com.example.clubportal.repository.ClubRepository;
import com.example.clubportal.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final UserService userService;
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club getClubById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club not found with id: " + id));
    }

    public Club getClubByName(String name) {
        return clubRepository.findByName(name)
                .orElseThrow(() -> new ClubNotFoundException("Club not found with name: " + name));
    }

    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    @Transactional
    public ClubDTO assignCoordinators(Long clubId, Set<Long> userIds) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        Set<User> users = userRepository.findAllById(userIds).stream().collect(Collectors.toSet());

        if (users.isEmpty()) {
            throw new UserNotFoundException("No valid users found for provided IDs");
        }

        // Remove existing coordinators first
        clubMemberRepository.deleteAllByClubAndRole(club, ClubMember.Role.COORDINATOR);

        // Convert Users to ClubMembers
        Set<ClubMember> coordinators = users.stream()
                .map(user -> new ClubMember(user, club, ClubMember.Role.COORDINATOR))
                .collect(Collectors.toSet());

        // Save new coordinators
        clubMemberRepository.saveAll(coordinators);

        club.setCoordinators(coordinators);
        clubRepository.save(club);

        return new ClubDTO(club);
    }

    public Club updateClub(Long id, Club updatedClub) {
        Club existingClub = getClubById(id);

        if (updatedClub.getName() != null && !updatedClub.getName().isEmpty()) {
            existingClub.setName(updatedClub.getName());
        }
        if (updatedClub.getDescription() != null && !updatedClub.getDescription().isEmpty()) {
            existingClub.setDescription(updatedClub.getDescription());
        }

        return clubRepository.save(existingClub);
    }

    public void deleteClub(Long id) {
        if (!clubRepository.existsById(id)) {
            throw new ClubNotFoundException("Club with ID " + id + " not found.");
        }
        clubRepository.deleteById(id);
    }

    public void removeMemberFromClub(Long clubId, Long userId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException("Club not found with ID: " + clubId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        ClubMember clubMember = clubMemberRepository.findByClubAndUser(club, user)
                .orElseThrow(() -> new ClubNotFoundException("User is not a member of this club"));

        clubMemberRepository.delete(clubMember);
    }

    
    public Set<User> getClubMembers(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));
    
        return club.getMembers().stream()
                .map(ClubMember::getUser) // Extract User from ClubMember
                .collect(Collectors.toSet());
    }
    public void promoteMemberToCoordinator(Long clubId, Long userId) {
        Club club = getClubById(clubId);
        User user = userService.getUserById(userId);

        if (!club.getMembers().contains(user)) {
            throw new RuntimeException("User is not a member of this club");
        }

        // Ensure role is changed
        user.setRole(ClubMember.Role.COORDINATOR);
        userService.saveUser(user);
    }

}
