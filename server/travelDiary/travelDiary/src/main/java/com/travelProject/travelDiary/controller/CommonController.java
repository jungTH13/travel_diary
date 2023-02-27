package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.entity.Country;
import com.travelProject.travelDiary.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CommonController {
    @Autowired
    private CommonService commonService;
    @GetMapping("/common/getListCountry")
    public List<Country> selectListCountry(){
        List<Country> selectListCountry = commonService.selectListCountry();
        return selectListCountry;
    }
}
