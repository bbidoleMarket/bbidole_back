package com.bbidoleMarket.bbidoleMarket.api.admin.controller;

import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminPostReportResDto;
import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminUserReportResDto;
import com.bbidoleMarket.bbidoleMarket.api.admin.service.AdminReportService;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.ReportStatus;
import com.bbidoleMarket.bbidoleMarket.common.config.RequireAdmin;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/report")
public class AdminReportController {
    private final AdminReportService adminReportService;

    //회원 신고 리스트 조회
    @RequireAdmin
    @GetMapping("/userlist")
    public ResponseEntity<ApiResponse<List<AdminUserReportResDto>>> userList(){
        List<AdminUserReportResDto> dto = adminReportService.getReportUserList();
        return ApiResponse.success(SuccessStatus.SUCCESS_USER_REPORT_FOUND,dto);
    }

    //회원 신고 상세 페이지
    @RequireAdmin
    @GetMapping("/userlist-detail/{reportId}")
    public ResponseEntity<ApiResponse<AdminUserReportResDto>> userReportDetail(@PathVariable Long reportId){
        AdminUserReportResDto dto = adminReportService.getReportUserDetail(reportId);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,dto);
    }

    //회원 신고 승인,거절
    @RequireAdmin
    @PutMapping("/user-status/{reportId}/status")
    public ResponseEntity<ApiResponse<Void>> modifyReportUSerStatus(@PathVariable Long reportId, @RequestParam ReportStatus status){
        adminReportService.modifyReportUserStatus(reportId,status);
        return ApiResponse.success_only(SuccessStatus.SEND_HEALTH_SUCCESS);
    }
    //게시물 신고 리스트
    @RequireAdmin
    @GetMapping("/postlist")
    public ResponseEntity<ApiResponse<List<AdminPostReportResDto>>>  postList(){
        List<AdminPostReportResDto> dto = adminReportService.getReportPostList();
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,dto);
    }
    //게시물 신고 상세페이지
    @RequireAdmin
    @GetMapping("/postlist-detail/{reportId}")
    public ResponseEntity<ApiResponse<AdminPostReportResDto>> postReportDetail(@PathVariable Long reportId){
        AdminPostReportResDto dto = adminReportService.getReportPostDetail(reportId);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,dto);
    }
    //게시물 신고 승인,거절
    @RequireAdmin
    @PutMapping("/post-status/{reportId}/status")
    public ResponseEntity<ApiResponse<Void>> modifyReportPostStatus(@PathVariable Long reportId, @RequestParam ReportStatus status){
        adminReportService.modifyReportPostStatus(reportId,status);
        return ApiResponse.success_only(SuccessStatus.SEND_HEALTH_SUCCESS);
    }
}
