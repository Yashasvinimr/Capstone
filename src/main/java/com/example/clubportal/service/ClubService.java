package com.example.clubportal.service;

import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.ClubMember;
import com.example.clubportal.entity.User;
import com.example.clubportal.exceptions.ClubNotFoundException;
import com.example.clubportal.exceptions.UserNotFoundException;
import com.example.clubportal.repository.ClubMemberRepository;
import com.example.clubportal.repository.ClubRepository;
import com.example.clubportal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ClubMemberRepository clubMemberRepository;

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
    public Club addCoordinators(Long clubId, Set<Long> userIds) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));

        Set<ClubMember> coordinators = userIds.stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("User not found"));
                    return new ClubMember(user, club, ClubMember.Role.COORDINATOR);
                })
                .collect(Collectors.toSet());

        clubMemberRepository.saveAll(coordinators);
        return club;
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
            throw new EntityNotFoundException("Club with ID " + id + " not found.");
        }
        clubRepository.deleteById(id);
    }
    public void removeMemberFromClub(Long clubId, Long userId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with ID: " + clubId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        ClubMember clubMember = clubMemberRepository.findByClubAndUser(club, user)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of this club"));

        clubMemberRepository.delete(clubMember);
    }

    public Set<User> getClubMembers(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));
        return club.getMembers(); // Assuming `Club` entity has a `Set<User> members`
    }



}
