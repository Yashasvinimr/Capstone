package com.example.clubportal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectMessageRequest {
    private Long senderId;
    private Long receiverId;
    private String messageContent;
}
