package com.example.clubportal.controller;

import com.example.clubportal.entity.ClubMember;
import com.example.clubportal.service.ClubMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs/{clubId}/members")
@RequiredArgsConstructor
public class ClubMemberController {
    private final ClubMemberService clubMemberService;

    // 📌 Get all APPROVED members of a club
    @GetMapping
    public ResponseEntity<List<ClubMember>> getMembersByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubMemberService.getMembersByClub(clubId));
    }

    // 📌 Get all PENDING members (for approval/rejection)
    @GetMapping("/pending")
    public ResponseEntity<List<ClubMember>> getPendingMembers(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubMemberService.getPendingMembers(clubId));
    }

    // 📌 User requests to join a club (PENDING status)
    @PostMapping("/{userId}/request")
    public ResponseEntity<ClubMember> requestToJoinClub(@PathVariable Long clubId, @PathVariable Long userId) {
        return ResponseEntity.ok(clubMemberService.requestToJoinClub(clubId, userId));
    }

    // 📌 Approve a pending member
    @PutMapping("/{memberId}/approve")
    public ResponseEntity<ClubMember> approveMember(@PathVariable Long clubId, @PathVariable Long memberId) {
        return ResponseEntity.ok(clubMemberService.approveMember(clubId, memberId));
    }

    // 📌 Reject a pending member
    @DeleteMapping("/{memberId}/reject")
    public ResponseEntity<Void> rejectMember(@PathVariable Long clubId, @PathVariable Long memberId) {
        clubMemberService.rejectMember(clubId, memberId);
        return ResponseEntity.noContent().build();
    }

    // 📌 Promote an APPROVED member to COORDINATOR
    @PutMapping("/{userId}/promote")
    public ResponseEntity<ClubMember> promoteToCoordinator(@PathVariable Long clubId, @PathVariable Long userId) {
        return ResponseEntity.ok(clubMemberService.promoteToCoordinator(clubId, userId));
    }

    // 📌 Remove a member from the club
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMemberFromClub(@PathVariable Long clubId, @PathVariable Long userId) {
        clubMemberService.removeMember(clubId, userId);
        return ResponseEntity.noContent().build();
    }
}
