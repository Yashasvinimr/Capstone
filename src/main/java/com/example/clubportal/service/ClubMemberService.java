package com.example.clubportal.service;

import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.ClubMember;
import com.example.clubportal.entity.User;
import com.example.clubportal.exceptions.ClubNotFoundException;
import com.example.clubportal.repository.ClubMemberRepository;
import com.example.clubportal.repository.ClubRepository;
import com.example.clubportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    


    @Autowired
    private UserRepository userRepository;

    

    public List<User> searchMembersByName(Long clubId, String query) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException("Club not found with id: " + clubId));

        List<ClubMember> clubMembers = clubMemberRepository.findByClub(club);

        return clubMembers.stream()
                .map(ClubMember::getUser)
                .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()))
                .map(user -> new User())
                .collect(Collectors.toList());
    }
        

    // Get all APPROVED members of a club
    public List<ClubMember> getMembersByClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        return clubMemberRepository.findByClubAndStatus(club, ClubMember.Status.APPROVED);
    }

    // Get all PENDING members waiting for approval
    public List<ClubMember> getPendingMembers(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        return clubMemberRepository.findByClubAndStatus(club, ClubMember.Status.PENDING);
    }

    // Add a user to a club (default: PENDING)
    @Transactional
    public ClubMember requestToJoinClub(Long clubId, Long userId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if user already requested or is a member
        Optional<ClubMember> existingMember = clubMemberRepository.findByClubAndUser(club, user);
        if (existingMember.isPresent()) {
            throw new IllegalStateException("User already requested or is a member of this club");
        }

        // Create new pending ClubMember entry
        ClubMember clubMember = new ClubMember(user, club, ClubMember.Role.MEMBER);
        clubMember.setStatus(ClubMember.Status.PENDING);
        return clubMemberRepository.save(clubMember);
    }

    // Approve a pending member
    @Transactional
    public ClubMember approveMember(Long clubId, Long memberId) {
        ClubMember member = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (!member.getClub().getId().equals(clubId)) {
            throw new IllegalArgumentException("Member does not belong to this club");
        }

        if (member.getStatus() != ClubMember.Status.PENDING) {
            throw new IllegalStateException("Member is not pending approval");
        }

        member.setStatus(ClubMember.Status.APPROVED);
        return clubMemberRepository.save(member);
    }

    // Reject a pending member
    @Transactional
    public void rejectMember(Long clubId, Long memberId) {
        ClubMember member = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (!member.getClub().getId().equals(clubId)) {
            throw new IllegalArgumentException("Member does not belong to this club");
        }

        if (member.getStatus() != ClubMember.Status.PENDING) {
            throw new IllegalStateException("Member is not pending approval");
        }

        clubMemberRepository.delete(member);
    }

    // // Promote a member to coordinator
    // @Transactional
    // public ClubMember promoteToCoordinator(Long clubId, Long userId) {
    //     Club club = clubRepository.findById(clubId)
    //             .orElseThrow(() -> new IllegalArgumentException("Club not found"));
    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new IllegalArgumentException("User not found"));

    //     ClubMember clubMember = clubMemberRepository.findByClubAndUser(club, user)
    //             .orElseThrow(() -> new IllegalStateException("User is not a member of this club"));

    //     // if (clubMember.getStatus() != ClubMember.Status.APPROVED) {
    //     //     throw new IllegalStateException("User must be an approved member before promotion");
    //     // }

    //     // if (clubMember.getRole() == ClubMember.Role.COORDINATOR) {
    //     //     throw new IllegalStateException("User is already a coordinator");
    //     // }

    //     clubMember.setRole(ClubMember.Role.COORDINATOR);
    //     return clubMemberRepository.save(clubMember);
    // }
    @Transactional
    public ClubMember promoteToCoordinator(Long clubId, Long userId) {
        ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId)
                .orElseThrow(() -> new IllegalStateException("User is not a member of this club"));

        clubMember.setRole(ClubMember.Role.COORDINATOR);
        return clubMemberRepository.save(clubMember);
    }


    // Remove a member from a club
    @Transactional
    public void removeMember(Long clubId, Long userId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ClubMember clubMember = clubMemberRepository.findByClubAndUser(club, user)
                .orElseThrow(() -> new IllegalStateException("User is not a member of this club"));

        clubMemberRepository.delete(clubMember);
    }
}
