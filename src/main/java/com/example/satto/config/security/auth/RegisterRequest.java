package com.example.satto.config.security.auth;

import com.example.satto.domain.users.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    //  private String profileImg;
    private String studentId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String department;
    private int grade;

    @JsonProperty("isPublic")
    private boolean isPublic;

}
