package com.travelProject.travelDiary.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserCotroller {

    @GetMapping("/test")
    public  String test(){
        return "test";
    }
}
