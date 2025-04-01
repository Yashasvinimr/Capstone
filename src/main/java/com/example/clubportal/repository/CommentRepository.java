package com.example.clubportal.repository;

import com.example.clubportal.entity.Comment;
import com.example.clubportal.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
