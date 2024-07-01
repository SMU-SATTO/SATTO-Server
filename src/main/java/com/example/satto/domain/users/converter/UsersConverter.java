package com.example.satto.domain.users.converter;

import com.example.satto.domain.users.dto.UsersResponseDTO;
import com.example.satto.domain.users.entity.Users;

public class UsersConverter {

    public static UsersResponseDTO.UserPreviewDTO toUserPreviewDTO(Users user) {
        return UsersResponseDTO.UserPreviewDTO.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .department(user.getDepartment())
                .grade(user.getGrade())
//                .profileImg(user.getProfileImg())
                .updateAt(user.getUpdatedAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static UsersResponseDTO.ExistUserDTO toUserShowDTO(Users user) {
        return UsersResponseDTO.ExistUserDTO.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .department(user.getDepartment())
                .grade(user.getGrade())
                .build();

    }

    public static UsersResponseDTO.UserProfileDTO toUserProfileDTO(Users user, int followerNum, int followingNum) {
        return UsersResponseDTO.UserProfileDTO.builder()
                .name(user.getName())
                .studentId(user.getStudentId())
                .followingNum(followingNum)
                .followerNum(followerNum)
                .build();
    }

    public static UsersResponseDTO.UserInformation2 toUserInformation2(Users user) {
        return UsersResponseDTO.UserInformation2.builder()
                .studentId(user.getStudentId())
                .name(user.getName())
                .nickname(user.getNickname())
                .department(user.getDepartment())
                .password(user.getPassword())
                .email(user.getEmail())
                .grade(user.getGrade())
                .isPublic(user.isPublic())
                .build();
    }

}
