package com.example.clubportal.controller;

import com.example.clubportal.dto.CommentRequest;
import com.example.clubportal.entity.Comment;
import com.example.clubportal.service.CommentService;
//import com.example.clubportal.service.PerspectiveService;
import com.example.clubportal.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

//    private final PerspectiveService perspectiveService;
//
//    public CommentController(PerspectiveService perspectiveService) {
//        this.perspectiveService = perspectiveService;
//    }

    /**
     * Add a comment to a specific post.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentRequest commentRequest) throws IOException {
        if (commentRequest.getPostId() == null || commentRequest.getContent() == null || commentRequest.getUserId() == null) {
            return ResponseEntity.badRequest().body("Post ID, User ID, and Comment text are required.");
        }

        // Check for profanity
//        boolean hasProfanity = perspectiveService.containsProfanity(commentRequest.getContent());
//        if (hasProfanity) {
//            return ResponseEntity.badRequest().body("Your comment contains inappropriate content.");
//        }

        commentService.createComment(commentRequest);
        return ResponseEntity.ok("Comment added successfully!");
    }


    /**
     * Get all comments for a specific post.
     */
    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    /**
     * Like a comment.
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId, @RequestParam Long userId) {
        postService.toggleLike(commentId, userId);
        return ResponseEntity.ok("Comment liked/unliked successfully!");
    }

    /**
     * Reply to a comment.
     */
    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Comment> replyToComment(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.replyToComment(commentId, request));
    }
}
