package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.PlanAirPlaneDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAirPlane;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import com.travelProject.travelDiary.service.plan.PlanAirPlaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanAirPlaneController {
    @Autowired
    private PlanAirPlaneService planAirPlaneService;

    @PostMapping("/travel/plan/airPlane/airPlaneOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        PlanAirPlane planAirPlaneOne= planAirPlaneService.selectPlanAirPlaneOne(planAirPlaneDto, user.getId());

        result.put("planAirPlaneOne", planAirPlaneOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/airPlane/airPlaneList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<PlanAirPlane> planAirPlaneList = planAirPlaneService.selectPlanAirPlaneList(planAirPlaneDto, user.getId());

        result.put("planAirPlaneList", planAirPlaneList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/airPlane/airPlaneInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto) {
        User user = (User) request.getAttribute("user");
        planAirPlaneDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planAirPlaneId = planAirPlaneService.planAirPlaneInsert(planAirPlaneDto);
        result.put("planAirPlaneId", planAirPlaneId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/plan/airPlane/airPlaneUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto) {
        User user = (User) request.getAttribute("user");
        planAirPlaneDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planAirPlaneId = planAirPlaneService.planAirPlaneUpdate(planAirPlaneDto);
        result.put("planAirPlaneId", planAirPlaneId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/plan/airPlane/airPlaneDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto) {
        User user = (User) request.getAttribute("user");
        planAirPlaneDto.setUser(user);

        planAirPlaneService.planAirPlaneDelete(planAirPlaneDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}