package com.bbidoleMarket.bbidoleMarket.api.admin.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.report.ChatRoomReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportUserRepository extends JpaRepository<ChatRoomReport,Long> {

    @Query("SELECT r FROM ChatRoomReport r JOIN FETCH r.reporter JOIN fetch r.reportedUser JOIN FETCH  r.chatRoom")
    List<ChatRoomReport> findAllWithUserAndChatRoom();

}
