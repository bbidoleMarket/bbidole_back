package com.bbidoleMarket.bbidoleMarket.api.admin.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.report.ChatRoomReport;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.PostReport;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportPostRepository extends JpaRepository<PostReport,Long> {
    @Query("SELECT r FROM PostReport r JOIN FETCH r.reporter JOIN FETCH  r.post")
    List<PostReport> findAllWithUserAndPost();

    long countByStatus(ReportStatus reportStatus);
}
