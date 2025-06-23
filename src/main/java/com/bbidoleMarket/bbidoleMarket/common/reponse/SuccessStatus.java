package com.bbidoleMarket.bbidoleMarket.common.reponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * 200
     */
    SEND_REGISTER_SUCCESS(HttpStatus.OK, "회원가입 성공"),
    SEND_HEALTH_SUCCESS(HttpStatus.OK, "서버 상태 OK"),

    SEARCH_POST_SUCCESS(HttpStatus.OK, "게시물 조회 성공"),
    UPDATE_POST_SUCCESS(HttpStatus.OK, "게시물 수정 성공"),
    SEARCH_USER_SUCCESS(HttpStatus.OK, "사용자 조회 성공"),
    SEARCH_REVIEW_SUCCESS(HttpStatus.OK, "리뷰 조회 성공"),
    SEARCH_CHAT_ROOM_SUCCESS(HttpStatus.OK, "채팅방 조회/생성 성공"),

    /**
     * 201
     */
    CREATE_RECRUIT_ARTICLE_SUCCESS(HttpStatus.CREATED, "게시글 등록 성공"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
