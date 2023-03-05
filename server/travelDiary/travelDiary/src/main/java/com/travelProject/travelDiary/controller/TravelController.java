package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TravelController {
    @Autowired
    private TravelService travelService;

    @GetMapping("/travel/getTravelList")
    public Map<String, Object>  getTravelList(){
        Map<String, Object> result = new HashMap<>();
        String userId = "";
        List<Map<String, Object>> selectPlanTravelList = travelService.selectPlanTravelList(userId);
        List<Map<String, Object>> selectEndTravelList = travelService.selectEndTravelList(userId);

        result.put("planTravel", selectPlanTravelList);
        result.put("endTravel", selectEndTravelList);
        return result;
    }

    @PostMapping("/travel/travelInsert")
    public Map<String, Object> setTravelInsert(@RequestBody Travel travel) {
        Map<String, Object> result = new HashMap<>();

        Long insertId = travel.getId();
        if(insertId == null){
            travelService.travelSave(travel);
            result.put("code", 200);
            result.put("msg", "저장 성공 했습니다.");
        } else {
            result.put("code", 400);
            result.put("msg", "저장 실패 했습니다.");
        }

        return result;
    }

    @PutMapping("/travel/travelUpdate")
    public Map<String, Object> setTravelUpdate(@RequestBody Travel travel) {
        Map<String, Object> result = new HashMap<>();
        travelService.travelSave(travel);
        result.put("code", 200);
        result.put("msg", "삭제 성공 했습니다.");
        return result;
    }

    @DeleteMapping("/travel/travelDelete")
    public String setTravelDelete(@RequestBody Travel travel) {
        travelService.travelDelete(travel);
        return "delete test";
    }

}
