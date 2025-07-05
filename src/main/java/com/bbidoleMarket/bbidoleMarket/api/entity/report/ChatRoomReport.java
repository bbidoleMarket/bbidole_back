package com.bbidoleMarket.bbidoleMarket.api.entity.report;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ChatRoom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;

    public static ChatRoomReport createChatRoomReport(
        User reporter,
        User reportedUser,
        String content,
        ChatRoom chatRoom
    ) {
        ChatRoomReport chatRoomReport = new ChatRoomReport();
        chatRoomReport.setReporter(reporter);
        chatRoomReport.setReportedUser(reportedUser);
        chatRoomReport.setContent(content);
        chatRoomReport.setStatus(ReportStatus.PENDING);
        chatRoomReport.chatRoom = chatRoom;
        return chatRoomReport;
    }

    @Override
    public void approveReport() {
        if (this.getStatus() == ReportStatus.PENDING) {
            this.setStatus(ReportStatus.APPROVED);
            // TODO 채팅방 판매자 차단 로직 추가
            chatRoom.getSeller().setIsActive(false); // 채팅방 판매자 차단
        } else {
            throw new IllegalStateException("Report must be in PENDING status to approve.");
        }
    }

    @Override
    public void revertReport() {
        if (this.getStatus() == ReportStatus.APPROVED
            || this.getStatus() == ReportStatus.REJECTED) {
            this.setStatus(ReportStatus.PENDING);
            // TODO 채팅방 판매자 차단 해제 로직 추가
            chatRoom.getSeller().setIsActive(true); // 채팅방 판매자 차단 해제
        } else {
            throw new IllegalStateException(
                "Report must be in APPROVED or REJECTED status to revert.");
        }
    }
}
