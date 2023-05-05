package com.travelProject.travelDiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),

    INVALID_USER_PARAMETER(400, "유저 값을 알수가 없습니다."),

    INVALID_ID_PARAMETER(400, "ID 값을 알수가 없습니다."),

    INVALID_TRAVEL_ID_PARAMETER(400, "여행 ID 값을 알수가 없습니다."),

    INVALID_IMAGE_ID_PARAMETER(400, "이미지 ID 값을 알수가 없습니다."),


    DIFFERENT_USER_PARAMETER(400, "본인 값만 수정 가능합니다."),

    //401 JWT
    INVALID_JWT_ERROR(401,"유저 토큰이 옳바르지 않습니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    DISPLAY_NOT_FOUND(404, "존재하지 않는 전시회 ID 입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_CREATE_ERROR(500,"데이터 생성 오류"),
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!");

    private final int status;
    private final String message;
}
