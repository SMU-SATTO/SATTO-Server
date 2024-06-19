package com.example.satto.config.security.auth;

import com.example.satto.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

  private final AuthenticationService service;

  @Operation(summary = "회원가입", description = "회원가입")
  @PostMapping("/register")
  public BaseResponse<String> register(
      @RequestBody RegisterRequest request
  ) {
    service.register(request);
    return BaseResponse.onSuccess("회원가입 성공");
  }

  @Operation(summary = "로그인", description = "로그인시 엑세스 토큰과 리프레시 토큰 발급")
  @PostMapping("/authenticate")  // 로그인시 토큰 재 발급
  public BaseResponse<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return BaseResponse.onSuccess(service.authenticate(request));
  }

  @Operation(summary = "리프레시 토큰 발급")
  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
