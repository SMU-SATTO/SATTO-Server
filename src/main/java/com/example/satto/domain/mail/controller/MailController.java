package com.example.satto.domain.mail.controller;

import com.example.satto.domain.mail.dto.EmailRequestDTO;
import com.example.satto.domain.mail.service.EmailService;
import com.example.satto.domain.users.dto.UsersRequestDTO;
import com.example.satto.domain.users.service.UsersService;
import com.example.satto.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users/id")
@RequiredArgsConstructor

public class MailController {

    private final EmailService emailService;
    private final UsersService usersService;

    // 인증번호 전송
    @Operation(summary = "인증번호 전송",
            description = "학번을 입력하면 해당 샘물 이메일 주소로 인증번호가 전송된다.")
    @PostMapping("/mail/check")
    public BaseResponse<Object> emailConfirm(@RequestBody EmailRequestDTO.EmailCheckRequest emailCheckRequest) throws Exception {
        if (usersService.studentIdDuplicate(emailCheckRequest)) {
            return BaseResponse.onFailure("해당 학번으로 이미 가입하셨습니다.");
        }

        String email = emailCheckRequest.getStudentId() + "@sangmyung.kr";
        emailService.sendSimpleMessage(email);
        return BaseResponse.onSuccess("인증번호 전송 성공");

    }

    @Operation(summary = "임시 비밀번호 전송",
            description = "샘물 이메일 주소로 임시 비밀번호가 전송된다.")
    @PostMapping("/findPw")
    public BaseResponse<Object> findPw(@RequestBody EmailRequestDTO.EmailCheckRequest emailCheckRequest) throws Exception {
        String email = emailCheckRequest.getStudentId() + "@sangmyung.kr";
        if (!usersService.studentIdDuplicate(emailCheckRequest)) {
            return BaseResponse.onFailure("존재하지 않는 이메일 입니다.");
        }
        String tempPw = emailService.sendFindPwMessage(email);

        UsersRequestDTO.UpdateUserPasswordDTO updateUserPasswordDTO = new UsersRequestDTO.UpdateUserPasswordDTO();
        updateUserPasswordDTO.setPassword(tempPw);
        Long userId = usersService.findId(email);
        usersService.resetPassword(updateUserPasswordDTO, userId);
        return BaseResponse.onSuccess("임시 비밀번호 전송 및 갱신 완료");
    }

    // 인증번호 확인
    @Operation(summary = "인증번호 확인",
            description = "이메일로 받은 인증번호를 입력한다.")
    @PostMapping("/mail/authentication")
    public BaseResponse<Object> emailAuthentication(@RequestBody EmailRequestDTO.EmailAuthRequest emailAuthRequest) {
        String email = emailAuthRequest.getEmail()+"@sangmyung.kr";
        String code = emailAuthRequest.getCertificationNum();
        if (emailService.verifyCode(email, code)) {
            return BaseResponse.onSuccess("인증번호가 일치합니다.");
        } else {
            return BaseResponse.onFailure("인증번호가 일치하지 않습니다.");
        }
    }
}

