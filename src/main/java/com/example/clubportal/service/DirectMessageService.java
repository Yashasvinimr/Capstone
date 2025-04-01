package com.example.clubportal.service;

import com.example.clubportal.dto.DirectMessageRequest;
import com.example.clubportal.entity.DirectMessage;
import com.example.clubportal.entity.User;
import com.example.clubportal.repository.DirectMessageRepository;
import com.example.clubportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    @Autowired
    private final DirectMessageRepository directMessageRepository;

    @Autowired
    private final UserRepository userRepository;

    // Send a new message
    public DirectMessage sendMessage(DirectMessageRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        DirectMessage message = new DirectMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(request.getMessageContent());
        message.setTimestamp(LocalDateTime.now());

        return directMessageRepository.save(message);
    }


    // Get all received messages for a user (excluding deleted ones)
    public List<DirectMessage> getMessagesForUser(Long userId) {
        return directMessageRepository.findByReceiverIdAndIsDeletedFalse(userId);
    }

    // Get chat history between two users
    public List<DirectMessage> getChatHistory(Long user1, Long user2) {
        return directMessageRepository.findBySenderIdAndReceiverIdAndIsDeletedFalseOrderByTimestamp(user1, user2);
    }

    // Mark a message as read
    public void markAsRead(Long messageId) {
        DirectMessage message = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        directMessageRepository.save(message);
    }

    // Soft delete a message
    public void deleteMessage(Long messageId) {
        DirectMessage message = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setDeleted(true);
        directMessageRepository.save(message);
    }
}
