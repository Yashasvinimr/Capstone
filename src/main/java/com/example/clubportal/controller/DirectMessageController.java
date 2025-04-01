package com.example.clubportal.controller;

import com.example.clubportal.dto.DirectMessageRequest;
import com.example.clubportal.entity.DirectMessage;
import com.example.clubportal.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class DirectMessageController {
    private final DirectMessageService directMessageService;

    // Send a message

    @PostMapping("/send")
    public ResponseEntity<DirectMessage> sendMessage(@RequestBody DirectMessageRequest request) {
        return ResponseEntity.ok(directMessageService.sendMessage(request));
    }


    // Get all messages received by a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<DirectMessage>> getUserMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(directMessageService.getMessagesForUser(userId));
    }

    // Get chat history between two users
    @GetMapping("/chat")
    public ResponseEntity<List<DirectMessage>> getChatHistory(
            @RequestParam Long user1, @RequestParam Long user2) {
        return ResponseEntity.ok(directMessageService.getChatHistory(user1, user2));
    }

    // Mark a message as read
    @PostMapping("/{messageId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long messageId) {
        directMessageService.markAsRead(messageId);
        return ResponseEntity.ok("Message marked as read");
    }

    // Soft delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId) {
        directMessageService.deleteMessage(messageId);
        return ResponseEntity.ok("Message deleted");
    }
}
