package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TravelController {
    @Autowired
    private TravelService travelService;

    @GetMapping("/travel/userTravelList")
    public ResponseBody  getTravelList(HttpServletRequest request){
        //User user = (User) request.getAttribute("user");
        //String userId = user.getId();

        Map<String,Object> map = (Map<String, Object>) request.getAttribute("user");
        String userId = map.get("id").toString();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectPlanTravelList = travelService.selectPlanTravelList(userId);
        List<Map<String, Object>> selectEndTravelList = travelService.selectEndTravelList(userId);

        result.put("planTravel", selectPlanTravelList);
        result.put("endTravel", selectEndTravelList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/travelInsert")
    public ResponseBody setTravelInsert(HttpServletRequest request, @RequestBody Travel travel) {
        Map<String,Object> map = (Map<String, Object>) request.getAttribute("user");
        String userId = map.get("id").toString();
        travel.setUserId(userId);

        travelService.travelInsert(travel);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").build();
    }

    @PutMapping("/travel/travelUpdate")
    public ResponseBody setTravelUpdate(HttpServletRequest request, @RequestBody Travel travel) {
        Map<String,Object> map = (Map<String, Object>) request.getAttribute("user");
        String userId = map.get("id").toString();

        travel.setUserId(userId);
        travelService.travelUpdate(travel);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").build();
    }

    @DeleteMapping("/travel/travelDelete")
    public ResponseBody setTravelDelete(HttpServletRequest request, @RequestBody Travel travel) {
        Map<String,Object> map = (Map<String, Object>) request.getAttribute("user");
        String userId = map.get("id").toString();

        travel.setUserId(userId);
        travelService.travelDelete(travel);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}