package com.bbidoleMarket.bbidoleMarket.api.entity.report;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("Post")
public class PostReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public static PostReport createPostReport(User reporter, String content, Post post) {
        PostReport postReport = new PostReport();
        postReport.setReporter(reporter);
        postReport.setContent(content);
        postReport.setStatus(ReportStatus.PENDING);
        postReport.post = post;
        return postReport;
    }

    @Override
    public void approveReport() {
        if (this.getStatus() == ReportStatus.PENDING) {
            this.setStatus(ReportStatus.APPROVED);
            post.markAsDeleted(); // post 삭제
        } else {
            throw new IllegalStateException("Report must be in PENDING status to approve.");
        }
    }

    @Override
    public void revertReport() {
        if (this.getStatus() == ReportStatus.APPROVED
            || this.getStatus() == ReportStatus.REJECTED) {
            this.setStatus(ReportStatus.PENDING);
            post.markAsUndeleted(); // post 복구
        } else {
            throw new IllegalStateException(
                "Report must be in APPROVED or REJECTED status to revert.");
        }
    }
}
