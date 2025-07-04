package com.bbidoleMarket.bbidoleMarket.api.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportReqDto {

    private long id; // 신고 ID
    private String type;
    private String content; // 신고 상세 설명
}