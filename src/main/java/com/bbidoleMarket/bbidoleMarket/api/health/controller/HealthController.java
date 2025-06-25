package com.bbidoleMarket.bbidoleMarket.api.health.controller;

import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HealthCheck", description = "HealthCheck 관련 API 입니다.")
@RestController
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "서버 상태 체크 API입니다.")
    public ResponseEntity<ApiResponse<Void>> healthCheck(){
        return ApiResponse.success_only(SuccessStatus.SEND_HEALTH_SUCCESS);
    }
}

