package com.bbidoleMarket.bbidoleMarket.api.admin.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.ReportStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor     @Builder
public class AdminUserReportResDto {
    private Long reportId;
    private String reporterName;  //신고자
    private String reportedUserName; //신고당한사람
    private String content; //신고 내용
    private ReportStatus reportStatus; //신고 상태
    private LocalDateTime createdAt; //신고 시간
    private LocalDateTime updateAt; //신고 처리 시간
    private Long chatRoomId; //채팅방 아이디

//    public AdminUserReportResDto(User user){
//        this.
//    }



}
