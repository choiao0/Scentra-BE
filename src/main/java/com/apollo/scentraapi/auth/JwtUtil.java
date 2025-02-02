package com.apollo.scentraapi.auth;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.auth.handler.JwtExceptionHandler;
import com.apollo.scentraapi.service.UserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Slf4j
@Component
public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 2 * 1000L; // access 2시간

    private SecretKey secretKey;
    private final UserDetailService userDetailService;

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey, UserDetailService userDetailService) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.userDetailService = userDetailService;
    }

    // 토큰 생성
    public String createAccessToken(Long userId, String userName) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("userName", userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();
    }

    // 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtExceptionHandler(ErrorStatus.TOKEN_EXPIRED.getMessage(), e);
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtExceptionHandler(ErrorStatus.WRONG_TYPE_SIGNATURE.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            throw new JwtExceptionHandler(ErrorStatus.WRONG_TYPE_TOKEN.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new JwtExceptionHandler(ErrorStatus.NOT_VALID_TOKEN.getMessage(), e);
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }
    public String getUserName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userName", String.class);
    }
}