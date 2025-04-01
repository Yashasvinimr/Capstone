package com.example.clubportal.repository;

import com.example.clubportal.entity.EventRegistration;
import com.example.clubportal.entity.User;
import com.example.clubportal.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByUser(User user);
    List<EventRegistration> findByEvent(Event event);
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);
}
