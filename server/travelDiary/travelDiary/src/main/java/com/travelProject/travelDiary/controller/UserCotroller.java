package com.travelProject.travelDiary.controller;



import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("user")
public class UserCotroller {

    @Autowired
    private UserService userService;

    @Value("${spring.cookie.name}")
    private String cookieName;

    @PostMapping("/cookie")
    public ResponseBody getCookie(HttpServletRequest request, HttpServletResponse response){
        if(request.getAttribute("user")!=null) return ResponseBody.builder().code(401).msg("guest-cookie가 이미 존재합니다.").build();

        String jwtToken = userService.getNewUserJWT();
        Cookie cookie = userService.wrapDataAtCookie(this.cookieName,jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500).msg("guest-cookie발행에 실패했습니다.").build();

        response.addCookie(cookie);
        return ResponseBody.builder().code(200).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/examCookie")
    public ResponseBody getExamCookie(HttpServletRequest request, HttpServletResponse response){

        String jwtToken = userService.getExamUserJWT();
        Cookie cookie = userService.wrapDataAtCookie(this.cookieName,jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500).msg("guest-cookie발행에 실패했습니다.").build();

        response.addCookie(cookie);
        return ResponseBody.builder().code(200).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/test")
    public  String test(){
        return "test5";
    }
}
