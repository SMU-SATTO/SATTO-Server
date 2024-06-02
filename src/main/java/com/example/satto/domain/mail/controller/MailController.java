package com.example.satto.domain.mail.controller;

import com.example.satto.domain.mail.dto.EmailRequestDTO;
import com.example.satto.domain.mail.service.EmailService;
import com.example.satto.domain.mail.service.EmailServiceImpl;
import com.example.satto.global.common.BaseResponse;
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

    // 인증번호 전송
    @PostMapping("/mail/check")
    public BaseResponse<Object> emailConfirm(@RequestBody EmailRequestDTO.EmailCheckRequest emailCheckRequest) throws Exception {
        String email = emailCheckRequest.getStudentId()+"@sangmyung.kr";
        String confirm;
        confirm = emailService.sendSimpleMessage(email);
        if (confirm.isEmpty()) {
            return BaseResponse.onFailure("인증번호 전송 실패");
        } else {
            return BaseResponse.onSuccess("인증번호 전송 성공");
        }
    }

    // 인증번호 확인
    @PostMapping("/mail/authentication")
    public BaseResponse<Object> emailAuthentication(@RequestBody EmailRequestDTO.EmailAuthRequest emailAuthRequest) {
        if (emailAuthRequest.getCertificationNum().equals(EmailServiceImpl.ePw)){
            return BaseResponse.onSuccess("인증번호가 일치합니다.");
        } else {
            return BaseResponse.onFailure("인증번호가 일치하지 않습니다.");
        }
    }
}
