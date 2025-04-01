package com.example.clubportal.service;

import com.example.clubportal.dto.CommentRequest;
import com.example.clubportal.entity.Comment;
import com.example.clubportal.entity.Post;
import com.example.clubportal.entity.User;
import com.example.clubportal.repository.CommentRepository;
import com.example.clubportal.repository.PostRepository;
import com.example.clubportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //vate ContentModerationService contentModerationService;


//    @Autowired
//    private PerspectiveService perspectiveService; // Ensure this is injected

    public Comment createComment(CommentRequest commentRequest) throws IOException {
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

//        if (perspectiveService.containsProfanity(commentRequest.getContent())) {
//            throw new IllegalArgumentException("Inappropriate comment detected!");
//        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(commentRequest.getContent());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return commentRepository.findByPost(post);
    }

    public Comment replyToComment(Long commentId, CommentRequest request) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment reply = new Comment();
        reply.setContent(request.getContent());
        reply.setUser(user);
        reply.setPost(parentComment.getPost());
        reply.setParentComment(parentComment);

        return commentRepository.save(reply);
    }

}
