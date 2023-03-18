package com.travelProject.travelDiary.controller;



import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
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
    private String sameSite = "None";

    @PostMapping("/cookie")
    public ResponseBody getCookie(HttpServletRequest request, HttpServletResponse response,@RequestHeader(value="User-Agent", defaultValue="myBrowser") String userAgent){
        if(request.getAttribute("user")!=null) return ResponseBody.builder().code(401).msg("guest-cookie가 이미 존재합니다.").build();

        String jwtToken = userService.getNewUserJWT();
        Cookie cookie = userService.wrapDataAtCookie(this.cookieName,jwtToken);
        ResponseCookie cookieSecure = userService.wrapDataAtCookieSecure(this.cookieName,jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500).msg("guest-cookie발행에 실패했습니다.").build();
        String userAgentName = userAgent.split("/")[0];
        //postman 요청시 secure 해제 토큰 발송
        if(userAgentName.equals("PostmanRuntime")) response.addCookie(cookie);
        else response.addHeader("Set-Cookie",cookieSecure.toString());

        return ResponseBody.builder().code(200).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/examCookie")
    public ResponseBody getExamCookie(HttpServletRequest request, HttpServletResponse response,@RequestHeader(value="User-Agent", defaultValue="myBrowser") String userAgent){

        String jwtToken = userService.getExamUserJWT();
        Cookie cookie = userService.wrapDataAtCookie(this.cookieName,jwtToken);
        ResponseCookie cookieSecure = userService.wrapDataAtCookieSecure(this.cookieName,jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500).msg("guest-cookie발행에 실패했습니다.").build();
        String userAgentName = userAgent.split("/")[0];
        //postman 요청시 secure 해제 토큰 발송
        if(userAgentName.equals("PostmanRuntime")) response.addCookie(cookie);
        else response.addHeader("Set-Cookie",cookieSecure.toString());

        return ResponseBody.builder().code(200).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/test")
    public  String test(){
        return "test5";
    }
}
