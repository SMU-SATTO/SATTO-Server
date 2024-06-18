package com.example.satto.config.security.auth;

import com.example.satto.global.common.BaseResponse;
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

  @PostMapping("/register") // 회원가입 후 토큰 발급
  public BaseResponse<String> register(
      @RequestBody RegisterRequest request
  ) {
    service.register(request);
    return BaseResponse.onSuccess("회원가입 성공");
  }

  @PostMapping("/authenticate")  // 로그인시 토큰 재 발급
  public BaseResponse<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return BaseResponse.onSuccess(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}