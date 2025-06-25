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
    SEND_REGISTER_SUCCESS(HttpStatus.OK,"회원가입 성공"),
    SEND_HEALTH_SUCCESS(HttpStatus.OK,"서버 상태 OK"),
    SEND_LOGIN_SUCCESS(HttpStatus.OK,"로그인 성공"),
    SEND_LOGOUT_SUCCESS(HttpStatus.OK,"로그아웃 성공"),
    SEND_TOKEN_REFRESH_SUCCESS(HttpStatus.OK,"토큰 갱신 성공"),
    SEND_USER_INFO_SUCCESS(HttpStatus.OK,"사용자 정보 조회 성공"),
    SEND_EMAIL_CHECK_SUCCESS(HttpStatus.OK,"이메일 사용 가능"),
    SEND_NICKNAME_CHECK_SUCCESS(HttpStatus.OK,"닉네임 사용 가능"),
    CREATE_POST_SUCCESS(HttpStatus.CREATED, "게시글 등록 성공"),
    GET_POST_LIST_SUCCESS(HttpStatus.OK, "물품 목록 조회 성공"),
    SEARCH_POST_SUCCESS(HttpStatus.OK, "물품 검색 성공"),

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
