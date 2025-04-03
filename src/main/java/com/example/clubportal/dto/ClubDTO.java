package com.example.clubportal.dto;

import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class ClubDTO {
    private Long id;
    private String name;
    private String description;
    private Set<String> coordinators; // Using Set to remove duplicates

    public ClubDTO(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.description = club.getDescription();
        this.coordinators = club.getCoordinators().stream()
                .map(User::getName) // Extract user names
                .collect(Collectors.toSet()); // Convert to Set to remove duplicates
    }
    

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Set<String> getCoordinators() { return coordinators; }
}
