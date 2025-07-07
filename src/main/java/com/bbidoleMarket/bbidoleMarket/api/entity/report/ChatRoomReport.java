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
import lombok.ToString;

@Entity
@DiscriminatorValue("ChatRoom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ChatRoomReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;

    public static ChatRoomReport createChatRoomReport(
        User reporter,
        User reportee,
        String content,
        ChatRoom chatRoom
    ) {
        ChatRoomReport chatRoomReport = new ChatRoomReport();
        chatRoomReport.setReporter(reporter);
        chatRoomReport.setReportedUser(reportee);
        chatRoomReport.setContent(content);
        chatRoomReport.setStatus(ReportStatus.PENDING);
        chatRoomReport.chatRoom = chatRoom;
        return chatRoomReport;
    }

    @Override
    public void approveReport() {
        if (this.getStatus() == ReportStatus.PENDING) {
            this.setStatus(ReportStatus.APPROVED);
            this.getReportedUser().deactivate();
        } else {
            throw new IllegalStateException("Report must be in PENDING status to approve.");
        }
    }

    @Override
    public void revertReport() {
        if (this.getStatus() == ReportStatus.APPROVED
            || this.getStatus() == ReportStatus.REJECTED) {
            this.setStatus(ReportStatus.PENDING);
            getReportedUser().active();
        } else {
            throw new IllegalStateException(
                "Report must be in APPROVED or REJECTED status to revert.");
        }
    }
}
