package com.example.clubportal.repository;

import com.example.clubportal.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPublicPostTrue();
    List<Post> findByClubId(Long clubId);
    List<Post> findByTagsContaining(String tag);
    @Query("SELECT COUNT(p) > 0 FROM Post p JOIN p.likedByUsers u WHERE p.id = :postId AND u.id = :userId")
    boolean isPostLikedByUser(@Param("postId") Long postId, @Param("userId") Long userId);
}
