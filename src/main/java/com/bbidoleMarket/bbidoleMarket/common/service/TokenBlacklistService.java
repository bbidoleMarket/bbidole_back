package com.bbidoleMarket.bbidoleMarket.common.service;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    
    // 블랙리스트된 토큰들을 저장 (실제 운영환경에서는 Redis 사용 권장)
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    
    // 스케줄러 (블랙리스트 전체 청소용)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public TokenBlacklistService() {
        // 1시간마다 블랙리스트 전체 청소
        scheduler.scheduleAtFixedRate(this::clearAllTokens, 1, 1, TimeUnit.HOURS);
    }
    
    /**
     * 토큰을 블랙리스트에 추가
     * @param token JWT 토큰
     */
    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }
    
    /**
     * 토큰이 블랙리스트에 있는지 확인
     * @param token JWT 토큰
     * @return 블랙리스트에 있으면 true
     */
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    
    /**
     * 토큰을 블랙리스트에서 제거 (수동 제거용)
     * @param token JWT 토큰
     */
    public void removeFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }
    
    /**
     * 블랙리스트 전체 청소 (1시간마다 자동 실행)
     * 이유: 블랙리스트에 있는 모든 토큰은 이미 무효화되었으므로
     * 굳이 개별 만료시간까지 보관할 필요 없음
     */
    private void clearAllTokens() {
        int clearedCount = blacklistedTokens.size();
        blacklistedTokens.clear();
        System.out.println("블랙리스트 전체 청소 완료 - 제거된 토큰 수: " + clearedCount + " (" + new Date() + ")");
    }
    
    /**
     * 블랙리스트 크기 반환 (모니터링용)
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
    
    /**
     * 모든 블랙리스트 토큰 제거 (테스트용 - 수동 호출)
     */
    public void clearAll() {
        blacklistedTokens.clear();
    }
} 