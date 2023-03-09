package com.travelProject.travelDiary.dto;

import lombok.*;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class ResponseBody<T> {
    private int code;
    private String msg;
    private T results;

    @Builder
    public ResponseBody(int code,String msg, T results){
        this.code = code;
        this.msg = msg;
        this.results = results;
    }

}
