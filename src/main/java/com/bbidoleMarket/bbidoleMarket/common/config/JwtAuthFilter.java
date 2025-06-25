package com.bbidoleMarket.bbidoleMarket.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 토큰을 검증하고 인증 정보를 설정하는 필터 클래스
 * 모든 HTTP 요청에 대해 쿠키에서 JWT 토큰을 확인하여 유효한 JWT 토큰이 있을 경우 인증을 처리합니다.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 생성자를 통한 의존성 주입
     *
     * @param jwtUtil JWT 토큰 검증을 위한 유틸리티
     */
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 필터 동작을 정의하는 메서드
     * 쿠키에서 JWT 토큰을 추출하고 유효성을 검증한 후,
     * 유효한 토큰이면 Spring Security의 SecurityContext에 Authentication 객체를 설정합니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri.equals("/api/auth/login") || uri.equals("/api/auth/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = null;
        String header = request.getHeader("Authorization");
        String jwt = null;
        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
        } else {
            filterChain.doFilter(request, response);
            return;
        }
        boolean isRefreshEndpoint = request.getRequestURI().equals("/api/auth/refresh");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰이 있는 경우 처리
        if (jwt != null) {
            try {
                // 토큰 타입 검증 (엔드포인트에 따라 다르게)
                if (isRefreshEndpoint) {
                    if (!jwtUtil.isRefreshToken(jwt)) {
                        logger.error("리프레시 토큰이 아닙니다");
                        sendErrorResponse(response, "INVALID_TOKEN_TYPE", "리프레시 토큰이 아닙니다");
                        return;
                    }
                } else {
                    if (!jwtUtil.isAccessToken(jwt)) {
                        logger.error("액세스 토큰이 아닙니다");
                        sendErrorResponse(response, "INVALID_TOKEN_TYPE", "액세스 토큰이 아닙니다");
                        return;
                    }
                }

                // 토큰에서 사용자 ID 추출 (Long ID를 String으로)
                userId = jwtUtil.extractUserId(jwt);

            } catch (ExpiredJwtException e) {
                logger.error("토큰이 만료되었습니다", e);
                sendErrorResponse(response, "TOKEN_EXPIRED", "토큰이 만료되었습니다");
                return;
            } catch (MalformedJwtException e) {
                logger.error("잘못된 형식의 토큰입니다", e);
                sendErrorResponse(response, "INVALID_TOKEN", "잘못된 형식의 토큰입니다");
                return;
            } catch (SignatureException e) {
                logger.error("토큰 서명이 유효하지 않습니다", e);
                sendErrorResponse(response, "INVALID_SIGNATURE", "토큰 서명이 유효하지 않습니다");
                return;
            } catch (UnsupportedJwtException e) {
                logger.error("지원되지 않는 토큰입니다", e);
                sendErrorResponse(response, "UNSUPPORTED_TOKEN", "지원되지 않는 토큰입니다");
                return;
            } catch (IllegalArgumentException e) {
                logger.error("토큰이 비어있거나 null입니다", e);
                sendErrorResponse(response, "EMPTY_TOKEN", "토큰이 비어있거나 null입니다");
                return;
            } catch (Exception e) {
                logger.error("토큰 검증 중 알 수 없는 오류가 발생했습니다", e);
                sendErrorResponse(response, "TOKEN_ERROR", "토큰 검증 중 오류가 발생했습니다");
                return;
            }
        }

        // 사용자 ID가 추출되었고 현재 SecurityContext에 인증 정보가 없는 경우
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // 토큰 유효성 검증
                if (jwtUtil.validateToken(jwt, userId)) {
                    // 인증 객체 생성
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userId, null, new ArrayList<>());

                    // 인증 객체에 요청 세부 정보 설정
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.error("토큰 검증에 실패했습니다");
                    sendErrorResponse(response, "INVALID_TOKEN", "토큰 검증에 실패했습니다");
                    return;
                }
            } catch (Exception e) {
                logger.error("토큰 유효성 검증 중 오류가 발생했습니다", e);
                sendErrorResponse(response, "TOKEN_VALIDATION_ERROR", "토큰 유효성 검증 중 오류가 발생했습니다");
                return;
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 검증 실패 시 에러 응답을 보내는 메서드
     *
     * @param response HTTP 응답
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @throws IOException 입출력 예외
     */
    private void sendErrorResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("status", 401);
        errorResponse.put("timestamp", System.currentTimeMillis());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
