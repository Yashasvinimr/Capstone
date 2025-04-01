package com.example.clubportal.repository;

import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // For Explore Page
    List<Event> findByVisibility(Event.Visibility visibility);

    @Query("SELECT e FROM Event e WHERE e.club.id IN :clubIds")
    List<Event> findByClubIds(@Param("clubIds") List<Long> clubIds); // For You Page

    @Query("SELECT e FROM Event e WHERE e.eventDate >= :now")
    List<Event> findUpcomingEvents(@Param("now") LocalDateTime now);

    List<Event> findByClubIn(Set<Club> clubs);
}
