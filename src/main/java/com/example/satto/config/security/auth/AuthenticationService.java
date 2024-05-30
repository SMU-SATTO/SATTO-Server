package com.example.satto.config.security.auth;

import com.example.satto.config.security.token.Token;
import com.example.satto.config.security.token.TokenRepository;
import com.example.satto.config.security.token.TokenType;
import com.example.satto.config.security.userDetails.CustomUserDetails;
import com.example.satto.util.JwtUtil;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.domain.users.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsersRepository usersRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    public AuthenticationResponse register(RegisterRequest request) {
        Users user = Users.builder()
                .profileImg(request.getProfileImg())
                .name(request.getName())
                .nickname(request.getNickname())
                .department(request.getDepartment())
                .studentId(request.getStudentId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isPublic(request.getIsPublic())
                .role(request.getRole())
                .build();
        Users savedUser = usersRepository.save(user);
        log.info("User registered with email: {}", savedUser.getEmail());
        return AuthenticationResponse.builder()
                .build();
    }

    // 로그인
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Users user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String jwtToken = jwtUtil.createJwtAccessToken(customUserDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(customUserDetails);

        revokeAllUserTokens(user);
        saveUserRefreshToken(user, refreshToken); // 리프레시 토큰만 저장
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void saveUserRefreshToken(Users user, String refreshToken) {
        // 사용자와 같은 리프레시 토큰이 이미 있는지 확인
        Optional<Token> existingTokenOptional = tokenRepository.findByToken(refreshToken);

        if (existingTokenOptional.isPresent()) {
            // 이미 존재하는 토큰이면 업데이트
            Token existingToken = existingTokenOptional.get();
            existingToken.setToken(refreshToken);
            existingToken.setExpired(false);
            existingToken.setRevoked(false);
            tokenRepository.save(existingToken);
        } else {
            // 새로운 토큰을 생성하여 저장
            Token newToken = Token.builder()
                    .users(user)
                    .token(refreshToken)
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            tokenRepository.save(newToken);
        }
    }

    public void revokeAllUserTokens(Users user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getEmail());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtUtil.getEmail(refreshToken);

        if (userEmail != null) {
            var usersOptional = this.usersRepository.findByEmail(userEmail);
            if (usersOptional.isPresent()) {
                var user = usersOptional.get();

                // 리프레시 토큰의 유효성 검사
                if (jwtUtil.isTokenValid(refreshToken, new CustomUserDetails(user))) {
                    // 새로운 액세스 토큰 생성
                    var accessToken = jwtUtil.createJwtAccessToken(new CustomUserDetails(user));

                    // 액세스 토큰 저장 필요 없음, 리프레시 토큰 유지
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();

                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
                }
            } else {
                // 사용자가 존재하지 않는 경우에 대한 처리
                throw new NoSuchElementException("User not found for email: " + userEmail);
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
        }
    }

}
