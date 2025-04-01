package com.example.clubportal.service;

import com.example.clubportal.entity.Club;
import com.example.clubportal.entity.ClubMember;
import com.example.clubportal.entity.Event;
import com.example.clubportal.entity.User;
import com.example.clubportal.repository.ClubRepository;
import com.example.clubportal.repository.EventRepository;
import com.example.clubportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getExploreEvents() {
        return eventRepository.findByVisibility(Event.Visibility.PUBLIC);
    }
    public List<Event> getForYouEvents(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Club> userClubs = user.getClubMemberships()
                .stream()
                .map(ClubMember::getClub) // Extract Club from ClubMember
                .collect(Collectors.toSet());// Assuming User entity has a Set<Club> field

        return eventRepository.findByClubIn(userClubs);
    }
    public Event createEvent(Long clubId, Event event) {
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isEmpty()) {
            throw new IllegalArgumentException("Club not found");
        }
        event.setClub(club.get());
        return eventRepository.save(event);
    }
}
