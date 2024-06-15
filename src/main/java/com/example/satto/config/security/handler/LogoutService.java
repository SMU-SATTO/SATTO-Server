package com.example.satto.config.security.handler;

import com.example.satto.config.security.token.TokenBlackList;
import com.example.satto.config.security.token.TokenBlackListRepository;
import com.example.satto.config.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;
  private final TokenBlackListRepository tokenBlackListRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      System.out.println("1=================");
      return;
    }
    jwt = authHeader.substring(7);
    var storedToken = tokenRepository.findByToken(jwt)
        .orElse(null);

    TokenBlackList blackList= new TokenBlackList();
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      blackList.setToken(jwt);
      System.out.println("여기=================jwt"+jwt);
      tokenRepository.save(storedToken);
      tokenBlackListRepository.save(blackList);
      SecurityContextHolder.clearContext();
    }
  }
}
