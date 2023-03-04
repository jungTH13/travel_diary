package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TravelController {
    @Autowired
    private TravelService travelService;

    @GetMapping("/travel/getTravelList")
    public Map<String, Object>  selectListCountry(){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectPlanTravelList = travelService.selectPlanTravelList();
        List<Map<String, Object>> selectEndTravelList = travelService.selectEndTravelList();

        result.put("planTravel", selectPlanTravelList);
        result.put("endTravel", selectEndTravelList);
        return result;
    }

    @PostMapping("/travel/setTravelInsert")
    public String setTravelInsert(@RequestBody Travel travel) {
        travelService.travelInsert(travel);
        return "test";
    }
}
