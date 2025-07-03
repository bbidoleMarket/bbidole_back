package com.bbidoleMarket.bbidoleMarket.common.config;

import com.bbidoleMarket.bbidoleMarket.common.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key key;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public JwtUtil(@Value("${jwt.secret.key}") String jwtSecret) {
        if(jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT Secret is required");
        }

        if(jwtSecret.length() < 64) {
            throw new IllegalArgumentException("JWT Secret must be at least 64 characters");
        }

        this.key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    @Value("${jwt.access-token.expiration}") // 1시간
    private long accessTokenValidityInMs;

    @Value("${jwt.refresh-token.expiration}") // 7일
    private long refreshTokenValidityInMs;

    public String extractUserId(String token) { return extractClaim(token, Claims::getSubject); }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;  // 파싱 실패 시 만료된 것으로 처리
        }
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public String generateAccessToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, userId, accessTokenValidityInMs);
    }

    public String generateAccessToken(String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        claims.put("role", role);
        return createToken(claims, userId, accessTokenValidityInMs);
    }

    public String generateToken(String userId) {
        return generateAccessToken(userId);
    }

    public String generateRefreshToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userId, refreshTokenValidityInMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long validityInMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, String userId) {
        // 토큰이 블랙리스트에 있는지 확인
        if (tokenBlacklistService.isBlacklisted(token)) {
            return false;
        }
        
        final String extractedUserId = extractUserId(token);
        return (extractedUserId.equals(userId) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        try {
            // 토큰이 블랙리스트에 있는지 확인
            if (tokenBlacklistService.isBlacklisted(token)) {
                return false;
            }
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return extractUserId(token);
    }

    public String getEmailFromToken(String token) {
        return extractUserId(token);
    }

    public String getRoleFromToken(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Boolean isAccessToken(String token) {
        String tokenType = extractTokenType(token);
        return "access".equals(tokenType);
    }

    public Boolean isRefreshToken(String token) {
        String tokenType = extractTokenType(token);
        return "refresh".equals(tokenType);
    }

    public String refreshAccessToken(String refreshToken) {
        if (!isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다");
        }

        String userId = extractUserId(refreshToken);
        if (!validateToken(refreshToken, userId)) {
            throw new IllegalArgumentException("만료되거나 유효하지 않은 리프레시 토큰입니다");
        }

        return generateAccessToken(userId);
    }

    /**
     * 토큰을 무효화 (블랙리스트에 추가)
     * @param token 무효화할 JWT 토큰
     */
    public void invalidateToken(String token) {
        // 간단하게 블랙리스트에 추가만 하면 됨
        // 1시간마다 전체 청소되므로 만료시간 관리 불필요
        tokenBlacklistService.addToBlacklist(token);
    }

    /**
     * 액세스 토큰과 리프레시 토큰을 모두 무효화
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레시 토큰
     */
    public void invalidateTokens(String accessToken, String refreshToken) {
        if (accessToken != null) {
            invalidateToken(accessToken);
        }
        if (refreshToken != null) {
            invalidateToken(refreshToken);
        }
    }

    /**
     * 특정 사용자의 모든 토큰을 무효화 (추후 확장 가능)
     * 현재는 개별 토큰만 무효화 가능
     * @param userId 사용자 ID
     */
    public void invalidateAllUserTokens(String userId) {
        // 현재 구현에서는 개별 토큰 추적이 어려움
        // Redis나 DB를 사용하면 사용자별 토큰 관리 가능
        // 향후 확장을 위해 메서드만 정의
    }

    /**
     * 토큰이 무효화되었는지 확인
     * @param token JWT 토큰
     * @return 무효화된 토큰이면 true
     */
    public boolean isTokenInvalidated(String token) {
        return tokenBlacklistService.isBlacklisted(token);
    }
}
