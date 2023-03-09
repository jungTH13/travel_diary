package com.travelProject.travelDiary.config;

import com.travelProject.travelDiary.dto.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.travelProject.travelDiary.dto.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ exceptionCode.class })
    protected ResponseBody handleCustomException(exceptionCode ex) {
        int status = ex.getErrorCode().getStatus();
        String errorMessage = ex.getErrorCode().getMessage();
        String httpValue = String.valueOf(HttpStatus.valueOf(ex.getErrorCode().getStatus()));

        return ResponseBody.builder().code(status).msg(errorMessage).results(httpValue).build();
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseBody handleServerException(Exception ex) {
        int status = INTERNAL_SERVER_ERROR.getStatus();
        String errorMessage = INTERNAL_SERVER_ERROR.getMessage();
        String httpValue = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseBody.builder().code(status).msg(errorMessage).results(httpValue).build();
    }
}
