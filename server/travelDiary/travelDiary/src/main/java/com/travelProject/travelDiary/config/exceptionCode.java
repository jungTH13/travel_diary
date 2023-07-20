package com.travelProject.travelDiary.config;

import com.travelProject.travelDiary.dto.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class exceptionCode extends RuntimeException {
    private final ErrorCode errorCode;

}
