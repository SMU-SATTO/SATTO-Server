package com.example.satto.config;

import com.example.satto.global.common.code.status.ErrorStatus;
import com.example.satto.global.common.exception.GeneralException;
import com.example.satto.token.Token;
import com.example.satto.token.TokenRepository;
import com.example.satto.userDetails.CustomUserDetails;
import com.example.satto.userDetails.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final TokenRepository tokenRepository;

    public JwtUtil(
            CustomUserDetailsService customUserDetailsService,
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.token.access-expiration-time}") Long access,
            @Value("${spring.jwt.token.refresh-expiration-time}") Long refresh,
            TokenRepository tokenRepo
    ) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        accessExpMs = access;
        refreshExpMs = refresh;
        tokenRepository = tokenRepo;
    }

    public Long getId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    // JWT 토큰을 입력으로 받아 토큰의 subject 로부터 사용자 Email 추출하는 메서드
    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    // JWT 토큰을 입력으로 받아 토큰의 claim 에서 사용자 권한을 추출하는 메서드
    public String getRole(String token) throws SignatureException{
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // JWT 토큰에서 만료기간 추출하는 메서드
    public long getExpTime(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime();
    }

    // Token 발급하는 메서드
    public String tokenProvider(CustomUserDetails customUserDetails, Instant expiration) {

        //현재 시간
        Instant issuedAt = Instant.now();

        //토큰에 부여할 권한
        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .header() //헤더 부분
                .add("typ", "JWT") // JWT type
                .and()
                .subject(customUserDetails.getUsername()) //Subject 에 username (email) 추가
                .claim("role", authorities) //권한 추가
                .issuedAt(Date.from(issuedAt)) // 현재 시간 추가
                .expiration(Date.from(expiration)) //만료 시간 추가
                .signWith(secretKey) //signature 추가
                .compact(); //합치기
    }

    // principalDetails 객체에 대해 새로운 JWT 액세스 토큰을 생성
    public String createJwtAccessToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(accessExpMs);
        return tokenProvider(customUserDetails, expiration);
    }

    // principalDetails 객체에 대해 새로운 JWT 리프레시 토큰을 생성
    public String createJwtRefreshToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(refreshExpMs);
        String refreshToken = tokenProvider(customUserDetails, expiration);

        // DB에 Refresh Token 저장
        tokenRepository.save(
                Token.builder()
                .email(customUserDetails.getUsername())
                .token(refreshToken)
                .build()
        );

        return refreshToken;
    }

    // 제공된 리프레시 토큰을 기반으로 JwtDto 쌍을 다시 발급
    public JwtDto reissueToken(String refreshToken) throws SignatureException {
        try {
            validateToken(refreshToken);
            log.info("[*] Valid RefreshToken");

            CustomUserDetails tempCustomUserDetails = new CustomUserDetails(
                    getId(refreshToken),
                    getEmail(refreshToken),
                    null,
                    getRole(refreshToken)
            );

            return new JwtDto(
                    createJwtAccessToken(tempCustomUserDetails),
                    createJwtRefreshToken(tempCustomUserDetails)
            );
        } catch (IllegalArgumentException iae) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        } catch (ExpiredJwtException eje) {
            throw new GeneralException(ErrorStatus.TOKEN_EXPIRED);
        }
    }
    private Claims getClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new GeneralException(ErrorStatus.TOKEN_SIGNATURE_ERROR);
        }
    }

    // HTTP 요청의 'Authorization' 헤더에서 JWT 액세스 토큰을 검색
    public String resolveAccessToken(HttpServletRequest request) {
        String tokenFromHeader = request.getHeader("Authorization");

        if (tokenFromHeader == null || !tokenFromHeader.startsWith("Bearer ")) {
            return null;
        }

        return tokenFromHeader.split(" ")[1]; //Bearer 와 분리
    }

    // 리프레시 토큰의 유효성을 검사
    public void validateToken(String token) {
        try {
            long seconds = 3 * 60;
            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(seconds)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());

        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new SecurityException("잘못된 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        }
    }

    //임시 방편용
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getEmail(token);
        return (username.equals(userDetails.getUsername())) && !validateTokenBool(token);
    }
    public boolean validateTokenBool(String token) {
        long seconds = 3 * 60;
        return Jwts
                .parser()
                .clockSkewSeconds(seconds)
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}