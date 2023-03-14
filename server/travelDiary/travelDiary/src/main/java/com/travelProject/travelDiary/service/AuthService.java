package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class AuthService {

    public User validateJWTUser(HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        if(user == null || user.getId() == null) throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);

        return user;
    }

}
