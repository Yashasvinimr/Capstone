package com.example.clubportal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
@Getter
@Setter
public class PostRequest {
    private Long userId;
    private Long clubId; // Nullable for public posts
    private String caption;
    private List<String> tags;
    private boolean publicPost; // Avoids "is" confusion
    private String imageUrl; // URL or upload logic later
}
