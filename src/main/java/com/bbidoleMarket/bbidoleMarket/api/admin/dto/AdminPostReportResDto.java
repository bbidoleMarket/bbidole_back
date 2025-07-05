package com.bbidoleMarket.bbidoleMarket.api.admin.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.report.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPostReportResDto {
    private String reporterName;  //신고자
    private String reportedPostName; //신고당한게시글
    private String content; //신고 내용
    private ReportStatus reportStatus; //신고 상태
    private LocalDateTime createdAt; //신고 시간
    private LocalDateTime updateAt; //신고 처리 시간
    private Long  postId; //게시글 아이디
    private String postTitle; //게시글 제목
}
