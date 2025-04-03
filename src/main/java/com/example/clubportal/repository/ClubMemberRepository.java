package com.example.clubportal.repository;

import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.ClubMember;
import com.example.clubportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    List<ClubMember> findByClubAndRole(Club club, String role);
    Optional<ClubMember> findByUserAndClub(User user, Club club);
    Optional<ClubMember> findByClubAndUser(Club club, User user);
    List<ClubMember> findByClubAndStatus(Club club, ClubMember.Status status); // Find members by club & status
    List<ClubMember> findByClub(Club club);
    List<ClubMember> findByUserId(Long userId);
    void deleteAllByClubAndRole(Club club, ClubMember.Role role);
    Optional<ClubMember>findByClubIdAndUserId(Long clubId, Long userId);
    
}
