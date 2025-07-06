package com.bbidoleMarket.bbidoleMarket.api.admin.service;

import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminPostReportResDto;
import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminUserReportResDto;

import com.bbidoleMarket.bbidoleMarket.api.admin.repository.ReportPostRepository;
import com.bbidoleMarket.bbidoleMarket.api.admin.repository.ReportUserRepository;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.ChatRoomReport;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.PostReport;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.Report;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.ReportStatus;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final ReportUserRepository reportUserRepository;
    private final ReportPostRepository reportPostRepository;

    //회원 신고 리스트 조회
    public List<AdminUserReportResDto> getReportUserList(){
        List<ChatRoomReport> raw = reportUserRepository.findAll();
        List<ChatRoomReport> reports = reportUserRepository.findAllWithUserAndChatRoom();
        List<AdminUserReportResDto> dtos = new ArrayList<>();
        for(ChatRoomReport report : reports){
            log.error(String.valueOf(report.getId()));
            AdminUserReportResDto dto = AdminUserReportResDto.builder()
                    .reportId(report.getId())
                    .reportedUserName(report.getReportedUser().getName())
                    .reporterName(report.getReporter().getName())
                    .content(report.getContent())
                    .reportStatus(report.getStatus())
                    .createdAt(report.getCreatedAt())
                    .updateAt(report.getUpdatedAt())
                    .chatRoomId(report.getChatRoom().getId())
                    .build();
            dtos.add(dto);

        }return dtos;
    }
    //회원 신고 상세 페이지
    @Transactional
    public AdminUserReportResDto getReportUserDetail(Long reportId){
        ChatRoomReport report = reportUserRepository.findById(reportId)
                .orElseThrow(() -> new BadRequestException("Report not found: " + reportId));
        AdminUserReportResDto dto = AdminUserReportResDto.builder()
                .reportId(report.getId())
                .reportedUserName(report.getReportedUser().getName())
                .reporterName(report.getReporter().getName())
                .content(report.getContent())
                .reportStatus(report.getStatus())
                .createdAt(report.getCreatedAt())
                .updateAt(report.getUpdatedAt())
                .chatRoomId(report.getChatRoom().getId())
                .build();

        System.out.println("회원 신고 상세 페이지 : " + dto);
        return dto;
    }

    //회원 신고 승인,거절
    @Transactional
    public void modifyReportUserStatus(Long reportId, ReportStatus newStatus){
        ChatRoomReport report = reportUserRepository.findById(reportId)
                .orElseThrow(() -> new BadRequestException("Report not found: " + reportId));
        switch (newStatus) {
            case APPROVED -> report.approveReport();
            case REJECTED -> report.rejectReport();
            case PENDING -> report.revertReport();
        }
    }
    //게시물 신고 리스트
    @Transactional
    public List<AdminPostReportResDto> getReportPostList(){
        List<PostReport> reports = reportPostRepository.findAllWithUserAndPost();
        List<AdminPostReportResDto> dtos = new ArrayList<>();
        for(PostReport report : reports){
            AdminPostReportResDto dto = AdminPostReportResDto.builder()
                    .postTitle(report.getPost().getTitle())
                    .reporterName(report.getReporter().getName())
                    .content(report.getContent())
                    .reportStatus(report.getStatus())
                    .createdAt(report.getCreatedAt())
                    .updateAt(report.getUpdatedAt())
                    .postId(report.getPost().getId())
                    .reportId(report.getId())
                    .reportedPostName(report.getReportedUser().getName())
                    .build();
            dtos.add(dto);
        }return dtos;
    }
    //게시물 신고 상세페이지
    @Transactional
    public AdminPostReportResDto getReportPostDetail(Long reportId){
        PostReport report = reportPostRepository.findById(reportId)
                .orElseThrow(() -> new BadRequestException("Report not found: " + reportId));
        AdminPostReportResDto dto = AdminPostReportResDto.builder()
                .postTitle(report.getPost().getTitle())
                .reporterName(report.getReporter().getName())
                .reportedPostName(report.getReportedUser().getName())
                .content(report.getContent())
                .reportStatus(report.getStatus())
                .createdAt(report.getCreatedAt())
                .updateAt(report.getUpdatedAt())
                .postId(report.getPost().getId())
                .reportId(report.getId())
                .build();

        return dto;
    }
    //게시물 신고 승인,거절
    @Transactional
    public void modifyReportPostStatus(Long reportId, ReportStatus newStatus){
        PostReport report = reportPostRepository.findById(reportId)
                .orElseThrow(() -> new BadRequestException("Report not found: " + reportId));
        switch (newStatus) {
            case APPROVED -> report.approveReport();
            case REJECTED -> report.rejectReport();
            case PENDING -> report.revertReport();
        }
    }
}
