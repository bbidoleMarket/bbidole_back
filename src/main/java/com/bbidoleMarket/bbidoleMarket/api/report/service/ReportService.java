package com.bbidoleMarket.bbidoleMarket.api.report.service;


import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.ChatRoomReport;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.PostReport;
import com.bbidoleMarket.bbidoleMarket.api.entity.report.Report;
import com.bbidoleMarket.bbidoleMarket.api.login.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.ChatRoomRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.api.report.dto.ReportReqDto;
import com.bbidoleMarket.bbidoleMarket.api.report.repository.ReportRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Boolean saveReport(ReportReqDto dto, Long reporterId) {
        log.error(dto.getType());
        ReportType type = ReportType.fromString(dto.getType());
        User reporter = userRepository.findById(reporterId)
            .orElseThrow(
                () -> new BadRequestException("Reporter not found with ID: " + reporterId));
        Report report;

        switch (type) {
            case CHAT_ROOM -> {
                Long chatRoomId = dto.getId();
                ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                    () -> new NotFoundException("Chat room not found with ID: " + chatRoomId));
                User reported = userRepository.findById(chatRoom.getSeller().getId()).get();
                report = ChatRoomReport.createChatRoomReport(
                    reporter,
                    reported,
                    dto.getContent(),
                    chatRoom
                );
            }
            case POST -> {
                Long postId = dto.getId();
                Post post = postRepository.findById(postId).orElseThrow(
                    () -> new NotFoundException("Post not found with ID: " + postId)
                );
                report = PostReport.createPostReport(
                    reporter,
                    post,
                    dto.getContent(),
                    post
                );
            }
            default -> throw new BadRequestException("Invalid report type: " + dto.getType());
        }

        reportRepository.save(report);
        return true;
    }

}
