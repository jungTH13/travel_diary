package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TravelController {
    @Autowired
    private TravelService travelService;

    @GetMapping("/travel/getTravelList")
    public Map<String, Object>  getTravelList(@RequestBody Travel travel){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectPlanTravelList = travelService.selectPlanTravelList(travel);
        List<Map<String, Object>> selectEndTravelList = travelService.selectEndTravelList(travel);

        result.put("planTravel", selectPlanTravelList);
        result.put("endTravel", selectEndTravelList);
        return result;
    }

    @PostMapping("/travel/setTravelSave")
    public String setTravelInsert(@RequestBody Travel travel) {
        travelService.travelSave(travel);
        return "save test";
    }

    @DeleteMapping("/travel/setTravelDelete")
    public String setTravelDelete(@RequestBody Travel travel) {
        travelService.travelDelete(travel);
        return "delete test";
    }

}
