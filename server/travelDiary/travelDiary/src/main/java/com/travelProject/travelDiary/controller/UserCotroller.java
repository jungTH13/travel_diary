package com.travelProject.travelDiary.controller;



import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.service.UserServie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("user")
public class UserCotroller {

    @Autowired
    private UserServie userServie;
    @Autowired
    private Environment env;

    @PostMapping("/cookie")
    public ResponseBody getCookie(HttpServletRequest request, HttpServletResponse response){
        if(request.getAttribute("user")!=null) return ResponseBody.builder().code(401L).msg("guest-cookie가 이미 존재합니다.").build();

        String jwtToken = userServie.getNewUserJWT();
        Cookie cookie = userServie.wrapDataAtCookie(env.getProperty("spring.cookie.name"),jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500L).msg("guest-cookie발행에 실패했습니다.").build();

        response.addCookie(cookie);
        return ResponseBody.builder().code(200L).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/test")
    public  String test(){
        return "test5";
    }
}
