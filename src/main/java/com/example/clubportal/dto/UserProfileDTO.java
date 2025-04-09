package com.example.clubportal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private int year;
    private String department;
}
