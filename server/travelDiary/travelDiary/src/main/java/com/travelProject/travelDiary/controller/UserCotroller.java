package com.travelProject.travelDiary.controller;



import com.travelProject.travelDiary.service.UserServie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("user")
public class UserCotroller {

    @Autowired
    private UserServie userServie;

    @PostMapping("/cookie")
    public ResponseEntity<Map<String,String>> getCookie(HttpServletResponse response){
        Cookie cookie = userServie.getNewGestCookie();

        Map<String,String> results = new HashMap<String,String>();

        if(cookie == null) {
            results.put("msg","failed");
        }
        else {
            results.put("msg","success");
            response.addCookie(cookie);
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(results);
    }

    @GetMapping("/test")
    public  String test(){
        return "test5";
    }
}
