package com.jobflow.jobflow.dto;

import com.jobflow.jobflow.enums.Role;
import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
