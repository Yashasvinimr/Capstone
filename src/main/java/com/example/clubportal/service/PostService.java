package com.example.clubportal.service;

import com.example.clubportal.dto.PostRequest;
import com.example.clubportal.entity.Post;
import com.example.clubportal.entity.User;
import com.example.clubportal.entity.Club;
import com.example.clubportal.repository.PostRepository;
import com.example.clubportal.repository.UserRepository;
import com.example.clubportal.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

//    @Autowired
//    private ContentModerationService contentModerationService;
    public Post createPost(PostRequest postRequest) throws IOException {
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Club club = (postRequest.getClubId() != null)
                ? clubRepository.findById(postRequest.getClubId()).orElse(null)
                : null;

        Post post = new Post();
        post.setUser(user);
        post.setClub(club);
        post.setCaption(postRequest.getCaption());
        //post.setTags((Set<String>) postRequest.getTags());

        post.setTags(new HashSet<>(postRequest.getTags())); // Convert List to Set

        post.setPublicPost(postRequest.isPublicPost());
        post.setImageUrl(postRequest.getImageUrl()); // Handle file upload separately
        System.out.println("Post isPublic before saving: " + post.isPublicPost());
//        if (!contentModerationService.isContentAppropriate(post.getCaption())) {
//            throw new IllegalArgumentException("Inappropriate content detected in caption!");
//        }
        return postRepository.save(post);
    }
    public void toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (post.getLikedByUsers().contains(user)) {
            post.getLikedByUsers().remove(user); // Unlike
        } else {
            post.getLikedByUsers().add(user); // Like
        }

        postRepository.save(post);
    }

    public boolean likePost(Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalPost.isPresent() && optionalUser.isPresent()) {
            Post post = optionalPost.get();
            User user = optionalUser.get();

            if (post.getLikedByUsers().contains(user)) {
                post.getLikedByUsers().remove(user); // Unlike if already liked
            } else {
                post.getLikedByUsers().add(user); // Like the post
            }
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public List<Post> getAllPublicPosts() {
        return postRepository.findByPublicPostTrue();
    }

    public List<Post> getClubPosts(Long clubId) {
        return postRepository.findByClubId(clubId);
    }

    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTagsContaining(tag);
    }
    public Set<User> getLikedUsers(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getLikedByUsers(); // Returns the list of users who liked the post
    }

}
