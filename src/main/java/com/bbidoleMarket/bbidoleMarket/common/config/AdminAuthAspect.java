package com.bbidoleMarket.bbidoleMarket.common.config;

import com.bbidoleMarket.bbidoleMarket.api.entity.Role;
import com.bbidoleMarket.bbidoleMarket.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminAuthAspect {
    
    private final JwtUtil jwtUtil;

    @Before("@annotation(requireAdmin)")
    public void checkAdminPermission(RequireAdmin requireAdmin) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException("토큰이 없습니다.");
        }
        
        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
        
        String role = jwtUtil.getRoleFromToken(token);
        if (!Role.ADMIN.getKey().equals(role)) {
            throw new UnauthorizedException(requireAdmin.message());
        }
    }
} 