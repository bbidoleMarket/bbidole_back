package com.bbidoleMarket.bbidoleMarket.api.report.controller;

import com.bbidoleMarket.bbidoleMarket.api.report.dto.ReportReqDto;
import com.bbidoleMarket.bbidoleMarket.api.report.service.ReportService;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "신고", description = "각종 신고에 관한 API")
@RestController
@Slf4j
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Boolean>> saveReport(
        @RequestBody ReportReqDto dto,
        @AuthenticationPrincipal String id
    ) {
        try {
            Long reporter = Long.parseLong(id);
            return ApiResponse.success(SuccessStatus.CREATE_REPORT_SUCCESS,
                reportService.saveReport(dto, reporter));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid user ID format: " + id);
        }
    }

}
