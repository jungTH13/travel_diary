package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("/common/getListCountry")
    public Map<String, Object>  selectListCountry(){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectListCountry = commonService.selectListCountry();
        result.put("contryList", selectListCountry);
        return result;
    }
}
