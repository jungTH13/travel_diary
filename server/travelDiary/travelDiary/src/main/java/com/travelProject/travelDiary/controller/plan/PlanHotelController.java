package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.PlanHotelDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
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

    @PostMapping("/travel/{travelId}/plan/hotel/hotelOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planHotelDto);
        PlanHotel planHotelOne= planHotelService.selectPlanHotelOne(planHotelDto, user.getId());

        result.put("planHotelOne", planHotelOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/hotel/hotelList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planHotelDto);
        List<PlanHotel> planHotelList = planHotelService.selectPlanHotelList(planHotelDto, user.getId());

        result.put("planHotelList", planHotelList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/hotel/hotelInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planHotelDto);
        Long planHotelId = planHotelService.planHotelInsert(planHotelDto);
        result.put("planHotelId", planHotelId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/hotel/hotelUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planHotelDto);
        Long planHotelId = planHotelService.planHotelUpdate(planHotelDto);
        result.put("planHotelId", planHotelId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/hotel/hotelDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        setTravelId(travelId, user, planHotelDto);
        planHotelService.planHotelDelete(planHotelDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanHotelDto setTravelId(Long travelId, User user, PlanHotelDto planHotelDto) {
        Travel travel = new Travel();
        travel.setId(travelId);

        planHotelDto.setTravel(travel);
        planHotelDto.setUser(user);
        return planHotelDto;
    }
}