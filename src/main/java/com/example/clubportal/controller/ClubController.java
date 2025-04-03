package com.example.clubportal.controller;

import com.example.clubportal.dto.ClubDTO;
import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.User;
import com.example.clubportal.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    @GetMapping
    public ResponseEntity<List<ClubDTO>> getAllClubs() {
        List<ClubDTO> clubs = clubService.getAllClubs().stream()
                .map(ClubDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable Long id) {
        Club club = clubService.getClubById(id);
        return ResponseEntity.ok(new ClubDTO(club));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClubDTO> getClubByName(@PathVariable String name) {
        Club club = clubService.getClubByName(name);
        return ResponseEntity.ok(new ClubDTO(club));
    }

    @PostMapping
    public ResponseEntity<ClubDTO> createClub(@RequestBody Club club) {
        System.out.println(club.getName() + " :  " + club.getDescription());
        Club newClub = clubService.createClub(club);
        System.out.println(newClub);
        return ResponseEntity.ok(new ClubDTO(newClub));
    }

    @PutMapping("/{clubId}/coordinators")
    public ResponseEntity<ClubDTO> assignCoordinators(
            @PathVariable Long clubId,
            @RequestBody Set<Long> userIds) {
        
        ClubDTO updatedClub = clubService.assignCoordinators(clubId, userIds);
        return ResponseEntity.ok(updatedClub);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubDTO> updateClub(@PathVariable Long id, @RequestBody Club updatedClub) {
        Club club = clubService.updateClub(id, updatedClub);
        return ResponseEntity.ok(new ClubDTO(club));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.ok("Club deleted successfully!");
    }

    @GetMapping("/{clubId}/members")
    public ResponseEntity<Set<User>> getClubMembers(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getClubMembers(clubId));
    }
}
