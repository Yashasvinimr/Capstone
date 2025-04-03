package com.example.clubportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clubs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ClubMember> members = new HashSet<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ClubMember> coordinators;


    // New Method to Fetch Coordinators
    public Set<User> getCoordinators() {
        if (members == null) return new HashSet<>(); // Ensure it's not null
        Set<User> coordinators = new HashSet<>();
        for (ClubMember cm : members) {
            if (cm.getRole() == ClubMember.Role.COORDINATOR) {
                coordinators.add(cm.getUser());
            }
        }
        return coordinators;
    }

   
}
