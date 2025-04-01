package com.example.clubportal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "club_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonIgnore
    private Club club;

    @Enumerated(EnumType.STRING) // Store role as STRING (MEMBER, COORDINATOR)
    @Column(name = "role", nullable = false)
    // @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonIgnore
    private Role role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public ClubMember(User user, Club club, Role role) {
        this.user = user;
        this.club = club;
        this.role = Role.valueOf(role.name().toUpperCase()); // Ensure uppercase
        this.joinedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // Default to PENDING

    public enum Role {
        MEMBER,
        COORDINATOR,
        ADMIN
    }

    public enum Status {
        PENDING, // Waiting for approval
        APPROVED, REJECTED // Successfully joined
    }
}
