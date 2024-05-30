package com.example.satto.auth;

import com.example.satto.config.JwtUtil;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.domain.users.repository.UsersRepository;
import com.example.satto.token.Token;
import com.example.satto.token.TokenRepository;
import com.example.satto.token.TokenType;
import com.example.satto.userDetails.CustomUserDetails;
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
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Users user, String jwtToken) {
        var token = Token.builder()
                .users(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Users user) {
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
        System.out.println(userEmail);
        System.out.println("*********************************");
        if (userEmail != null) {
            var usersOptional = this.usersRepository.findByEmail(userEmail);
            if (usersOptional.isPresent()) {
                var user = usersOptional.get();
                // 사용자가 존재하는 경우에 대한 처리
                if (jwtUtil.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtUtil.createJwtAccessToken(new CustomUserDetails(user));
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
            } else {
                // 사용자가 존재하지 않는 경우에 대한 처리
                throw new NoSuchElementException("User not found for email: " + userEmail);
            }
        }
    }
}
