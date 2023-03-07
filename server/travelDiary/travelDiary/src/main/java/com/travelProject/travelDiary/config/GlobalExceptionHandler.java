package com.travelProject.travelDiary.config;

import com.travelProject.travelDiary.dto.ReturnBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.travelProject.travelDiary.dto.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ exceptionCode.class })
    protected ReturnBody handleCustomException(exceptionCode ex) {
        Long status = (long)ex.getErrorCode().getStatus();
        String errorMessage = ex.getErrorCode().getMessage();
        String httpValue = String.valueOf(HttpStatus.valueOf(ex.getErrorCode().getStatus()));

        ReturnBody returnBody = new ReturnBody();
        returnBody.setCode(status);
        returnBody.setMsg(errorMessage);
        return returnBody;
    }

    @ExceptionHandler({ Exception.class })
    protected ReturnBody handleServerException(Exception ex) {
        Long status = (long) INTERNAL_SERVER_ERROR.getStatus();
        String errorMessage = INTERNAL_SERVER_ERROR.getMessage();
        String httpValue = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);

        ReturnBody returnBody = new ReturnBody();
        returnBody.setCode(status);
        returnBody.setMsg(errorMessage);
        return returnBody;
    }
}
