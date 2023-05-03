package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Country;
import com.travelProject.travelDiary.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("/common/countryList")
    public ResponseBody selectListCountry() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectListCountry = commonService.selectListCountry();
        result.put("countryList", selectListCountry);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @GetMapping("/common/countryOne")
    public ResponseBody selectCountry(@RequestBody Country country) {
        Map<String, Object> result = new HashMap<>();

        String code = country.getCode();
        List<Map<String, Object>> selectListCountry = commonService.selectCountry(code);
        result.put("countryList", selectListCountry);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }
}