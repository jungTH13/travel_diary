package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import com.travelProject.travelDiary.service.plan.PlanHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanHotelController {
    @Autowired
    private PlanHotelService planHotelService;

    @GetMapping("/plan/hotel/hotelPlanList")
    public ResponseBody  getPlanHotelList(HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectPlanHotelList = planHotelService.selectPlanHotelList(user.getId());

        result.put("PlanHotel", selectPlanHotelList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/plan/hotel/planHotelInsert")
    public ResponseBody setPlanHotelInsert(HttpServletRequest request, @RequestBody PlanHotel planHotel) {
        User user = (User) request.getAttribute("user");
        planHotel.setUser(user);

        planHotelService.planHotelInsert(planHotel);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").build();
    }

    @PutMapping("/plan/hotel/planHotelUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanHotel planHotel) {
        User user = (User) request.getAttribute("user");
        planHotel.setUser(user);

        planHotelService.planHotelUpdate(planHotel);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").build();
    }

    @DeleteMapping("/plan/hotel/planHotelDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanHotel planHotel) {
        User user = (User) request.getAttribute("user");
        planHotel.setUser(user);

        planHotelService.planHotelDelete(planHotel);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}