package com.example.clubportal.repository;

import com.example.clubportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    

    @Query("SELECT u FROM User u JOIN ClubMember cm ON u.id = cm.user.id WHERE cm.club.id = :clubId AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findUsersByClubIdAndName(@Param("clubId") Long clubId, @Param("name") String name);

    @Query("SELECT u FROM User u JOIN ClubMember cm ON u.id = cm.user.id " +
            "WHERE cm.club.id = :clubId AND " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> findUsersByClubAndName(@Param("clubId") Long clubId, @Param("query") String query);

}
