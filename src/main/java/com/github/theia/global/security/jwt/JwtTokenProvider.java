package com.github.theia.global.security.jwt;

import com.github.theia.adapter.in.rest.dto.respose.TokenResponse;
import com.github.theia.global.security.jwt.exception.ExpiredJwtException;
import com.github.theia.global.security.principle.AuthDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final AuthDetailsService authDetailsService;

    @Value("${spring.jwt.secretKey}")
    private String secretKey;
    @Value("${spring.jwt.refreshKey}")
    private String refreshKey;
    @Value("${spring.jwt.accessExp}")
    public Long accessExp;
    @Value("${spring.jwt.refreshExp}")
    public Long refreshExp;

    private static final String ACCESS_KEY = "access_token";
    private static final String REFRESH_KEY = "refresh_token";
    public static final String ACCESS_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refersh-Token";
    public static final String BEARER_PREFIX = "Bearer ";

    public TokenResponse getAccessToken(String email) {
        String accessToken = generateToken(email, accessExp);
        String refreshToken = generateRefrshToken(email, refreshExp);

        return new TokenResponse(accessToken, refreshToken);
    }

    private String generateToken(String email, long expiration) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject(email)
                .setHeaderParam("typ", ACCESS_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    private String generateRefrshToken(String email, long expiration) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, refreshKey)
                .setSubject(email)
                .setHeaderParam("typ", REFRESH_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader(ACCESS_HEADER);
        return parseToken(bearer);
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearer = request.getHeader(REFRESH_HEADER);
        return parseToken(bearer);
    }

    public String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.replace(BEARER_PREFIX, "");
        }
        return null;
    }

    public UsernamePasswordAuthenticationToken authorization(String token) {
        UserDetails userDetails = authDetailsService.loadUserByUsername(getTokenSubject(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getTokenSubject(String subject) {
        return getTokenBody(subject).getSubject();
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw ExpiredJwtException.EXCEPTION;
        }
    }
}
