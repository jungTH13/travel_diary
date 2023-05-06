package com.travelProject.travelDiary.controller;



import com.travelProject.travelDiary.dto.GoogleUserInfo;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.dto.UserInfo;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.AuthService;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("user")
public class UserCotroller {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Value("${spring.cookie.name}")
    private String cookieName;
    private String sameSite = "None";

    @PostMapping("/cookie")
    public ResponseBody getCookie(HttpServletRequest request, HttpServletResponse response,@RequestHeader(value="User-Agent", defaultValue="myBrowser") String userAgent){
        if(request.getAttribute("user")!=null) return ResponseBody.builder().code(402).msg("guest-cookie가 이미 존재합니다.").build();

        String jwtToken = userService.getNewUserJWT();

        return cookieCommonProcess(jwtToken,response,userAgent.split("/")[0]);
    }

    @GetMapping("/examCookie")
    public ResponseBody getExamCookie(HttpServletRequest request, HttpServletResponse response,@RequestHeader(value="User-Agent", defaultValue="myBrowser") String userAgent){

        String jwtToken = userService.getExamUserJWT();

        return cookieCommonProcess(jwtToken,response,userAgent.split("/")[0]);
    }

    @PostMapping("/googleOAuthLogin")
    public ResponseBody googleOAuthLogin (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, String> payload,
            @RequestHeader(value="User-Agent", defaultValue="myBrowser") String userAgent
        ) throws IOException {

        if(request.getAttribute("user")!=null) return ResponseBody.builder().code(402).msg("이미 로그인한 유저입니다.").build();

        //google OAuth의 유저 정보
        String code = payload.get("code");
        String token = authService.getGoogleToken(code);
        GoogleUserInfo googleUserInfo = authService.getGoogleUserInfo(token);

        // 유저 생성 및 JTW -> cookie 생성
        String jwtToken = userService.getNewUserJWT(googleUserInfo);

        return cookieCommonProcess(jwtToken,response,userAgent.split("/")[0]);
    }

    @GetMapping("/logout")
    public ResponseBody logout(HttpServletRequest request, HttpServletResponse response){
        if(request.getAttribute("user")==null) return ResponseBody.builder().code(401).msg("로그인 하지 않은 유저입니다.").build();

        ResponseCookie cookie = ResponseCookie
                .from(this.cookieName, null)
                .maxAge(0)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie",cookie.toString());

        return ResponseBody.builder().code(200).msg("정상 로그아웃 되었습니다.").build();
    }

    private ResponseBody cookieCommonProcess(String jwtToken,HttpServletResponse response){
        return cookieCommonProcess(jwtToken,response,"");
    }
    private ResponseBody cookieCommonProcess(String jwtToken,HttpServletResponse response,String userAgentName){
        if(jwtToken == null) return ResponseBody.builder().code(500).msg("cookie발행에 실패했습니다.").build();

        //postman 요청시 secure 해제 토큰 발송
        if(userAgentName.equals("PostmanRuntime")){
            Cookie cookie = userService.wrapDataAtCookie(this.cookieName,jwtToken);
            response.addCookie(cookie);
        }
        else{
            ResponseCookie cookieSecure = userService.wrapDataAtCookieSecure(this.cookieName,jwtToken);
            response.addHeader("Set-Cookie",cookieSecure.toString());
        }

        return ResponseBody.builder().code(200).msg("cookie가 발행되었습니다.").build();
    }

    @GetMapping("userInfo")
    public ResponseBody userInfo (HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();

        UserInfo userInfo = userService.getUserInfo(user.getId());

        result.put("userInfo",userInfo);

        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @GetMapping("/test")
    public  String test(){
        return "test5";
    }
}
