package com.travelProject.travelDiary.controller;



import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.service.UserServie;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;


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
        if(request.getAttribute("user")!=null) return ResponseBody.builder().code(401).msg("guest-cookie가 이미 존재합니다.").build();

        String jwtToken = userServie.getNewUserJWT();
        Cookie cookie = userServie.wrapDataAtCookie(env.getProperty("spring.cookie.name"),jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500).msg("guest-cookie발행에 실패했습니다.").build();

        response.addCookie(cookie);
        return ResponseBody.builder().code(200).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/examCookie")
    public ResponseBody getExamCookie(HttpServletRequest request, HttpServletResponse response){
        Date now = new Date();
        String jwtToken = Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setIssuer("travel")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(24*365).toMillis()))
                .claim("id","063e632c-1666-4d24-ad41-712911a499f6")
                .signWith(SignatureAlgorithm.HS256,"travel_diary")
                .compact();

        Cookie cookie = userServie.wrapDataAtCookie(env.getProperty("spring.cookie.name"),jwtToken);

        if(jwtToken == null || jwtToken.equals("")) return ResponseBody.builder().code(500).msg("guest-cookie발행에 실패했습니다.").build();

        response.addCookie(cookie);
        return ResponseBody.builder().code(200).msg("guest-cookie가 발행되었습니다.").build();
    }

    @GetMapping("/test")
    public  String test(){
        return "test5";
    }
}
