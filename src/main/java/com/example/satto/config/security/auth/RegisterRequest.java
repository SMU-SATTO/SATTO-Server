package com.example.satto.config.security.auth;

import com.example.satto.domain.users.Role;
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
    private int studentId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String department;
    private Byte isPublic;
}
