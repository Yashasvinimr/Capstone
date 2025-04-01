package com.example.clubportal.service;

import com.example.clubportal.entity.*;
import com.example.clubportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRegistrationService {
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ClubMemberRepository clubMemberRepository;

    public List<EventRegistration> getUserRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return eventRegistrationRepository.findByUser(user);
    }

    public List<EventRegistration> getEventRegistrations(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return eventRegistrationRepository.findByEvent(event);
    }

    @Transactional
    public EventRegistration registerForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        // Check if the user is already registered
        eventRegistrationRepository.findByUserAndEvent(user, event).ifPresent(reg -> {
            throw new IllegalStateException("User already registered for this event");
        });

        // Determine price based on membership
        boolean isMember = clubMemberRepository.findByClubAndUser(event.getClub(), user).isPresent();
        double price = isMember ? event.getRegistrationFeeMembers() : event.getRegistrationFeeNonMembers();

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setStatus(EventRegistration.Status.REGISTERED);
        registration.setPaymentStatus(EventRegistration.PaymentStatus.PENDING); // Default payment pending
        registration.setAmountPaid(price);

        return eventRegistrationRepository.save(registration);
    }

    @Transactional
    public void cancelRegistration(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        EventRegistration registration = eventRegistrationRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new IllegalStateException("User is not registered for this event"));

        // **Prevent cancellation if payment is completed**
        if (registration.getPaymentStatus() == EventRegistration.PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel registration as payment is already completed.");
        }

        eventRegistrationRepository.delete(registration);
    }

    @Transactional
    public void completePayment(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        EventRegistration registration = eventRegistrationRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new IllegalStateException("User is not registered for this event"));

        // **Mark payment as completed**
        registration.setPaymentStatus(EventRegistration.PaymentStatus.COMPLETED);
        eventRegistrationRepository.save(registration);
    }
}
