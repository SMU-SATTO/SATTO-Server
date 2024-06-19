package com.example.satto.domain.users.controller;

import com.example.satto.s31.FileFolder;
import com.example.satto.s31.FileService;
import com.example.satto.domain.users.converter.UsersConverter;
import com.example.satto.domain.users.dto.UsersRequestDTO;
import com.example.satto.domain.users.dto.UsersResponseDTO;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.domain.users.service.UsersService;
import com.example.satto.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsersController {

    private final UsersService usersService;
    private final FileService fileService;

    // email 중복 확인
    @Operation(summary = "email 중복 확인")
    @GetMapping("/id/{email}")
    public BaseResponse<?> emailDuplicate(@PathVariable("email") String email) {
        if (usersService.emailDuplicate(email)) {
            return BaseResponse.onSuccess("존재하는 아이디 입니다.");
        } else {
            return BaseResponse.onFailure("해당 아이디를 사용할 수 있습니다.");
        }
    }

    // nickname 중복 확인
    @Operation(summary = "nickname 중복 확인")
    @GetMapping("/id/nickname/{nickname}")
    public BaseResponse<?> nicknameDuplicate(@PathVariable("nickname") String nickname) {
        if (usersService.nicknameDuplicate(nickname)) {
            return BaseResponse.onSuccess("존재하는 닉네임 입니다.");
        } else {
            return BaseResponse.onFailure("해당 닉네임을 사용할 수 있습니다.");
        }
    }



//     프로필 이미지 등록
//    @PostMapping("/id/{email}/profile/image")
//    public BaseResponse<?> uploadProfileImg(@RequestParam("file") MultipartFile file, @PathVariable("email") String email) throws IOException {
//        String url = fileService.uploadFile(file, FileFolder.profile_Image);
//        usersService.uploadProfileImg(url, email);
//        return BaseResponse.onSuccess("프로필 사진 등록 완료");
//    }

//    // 이미지 삭제
//    @DeleteMapping("/id/{email}/profile/image")
//    public BaseResponse deleteProfileImg(@AuthenticationPrincipal Users user) {
//        String profileImg = user.getProfileImg();
//        fileService.deleteFile(profileImg);
//        return BaseResponse.onSuccess("삭제 완료");
//    }


    // 사용자 정보 조회
    @Operation(summary = "test용 사용자의 모든 정보 조회")
    @GetMapping("")
    public BaseResponse<?> userInformation(@AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        return BaseResponse.onSuccess(usersService.userInformation(userId));
    }

    // 개인정보 수정
    @Operation(summary = "개인정보 수정")
    @PatchMapping("account/update")
    public BaseResponse<UsersResponseDTO.UserPreviewDTO> updateAccount(@RequestBody UsersRequestDTO.UpdateUserDTO updateUserDTO, @AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        Users users = usersService.updateAccount(updateUserDTO, userId);
        return BaseResponse.onSuccess(UsersConverter.toUserPreviewDTO(users));
    }

    // 계정 비공개 설정
    @Operation(summary = "계정 비공개 설정", description = "true는 공개, false는 비공개")
    @PatchMapping("account/private")
    public BaseResponse<?> privateAccount(@AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        usersService.privateAccount(userId);
        return BaseResponse.onSuccess("비공개 설정 완료");
    }

    // 계정 공개 설정
    @Operation(summary = "계정 공개 설정", description = "true는 공개, false는 비공개")
    @PatchMapping("account/public")
    public BaseResponse<?> publicAccount(@AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        usersService.publicAccount(userId);
        return BaseResponse.onSuccess("공개 설정 완료");
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("account/withdrawal")
    public BaseResponse<?> withdrawal(@AuthenticationPrincipal Users user) {
//        Long userId = user.getUserId();
        usersService.withdrawal(user);
        return BaseResponse.onSuccess("계정 탈퇴 완료");
    }



}
