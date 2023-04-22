package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanAirPlaneDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAirPlane;
import com.travelProject.travelDiary.service.TravelService;
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

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/{travelId}/plan/airPlane/airPlaneOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planAirPlaneDto);
        PlanAirPlane planAirPlaneOne= planAirPlaneService.selectPlanAirPlaneOne(planAirPlaneDto, user.getId());

        result.put("planAirPlaneOne", planAirPlaneOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/airPlane/airPlaneList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody(required = false) PlanAirPlaneDto planAirPlaneDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planAirPlaneDto);
        List<PlanAirPlane> planAirPlaneList = planAirPlaneService.selectPlanAirPlaneList(planAirPlaneDto, user.getId());

        result.put("planAirPlaneList", planAirPlaneList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/airPlane/airPlaneInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planAirPlaneDto);
        Long planAirPlaneId = planAirPlaneService.planAirPlaneInsert(planAirPlaneDto);
        result.put("planAirPlaneId", planAirPlaneId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/airPlane/airPlaneUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanAirPlaneDto planAirPlaneDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planAirPlaneDto);
        Long planAirPlaneId = planAirPlaneService.planAirPlaneUpdate(planAirPlaneDto);
        result.put("planAirPlaneId", planAirPlaneId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/airPlane/airPlaneDelete/{planId}")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request
            , @RequestBody PlanAirPlaneDto planAirPlaneDto
            , @PathVariable Long travelId
            , @PathVariable Long planId) {
        User user = (User) request.getAttribute("user");

        planAirPlaneDto.setId(planId);
        setTravelId(travelId, user, planAirPlaneDto);
        planAirPlaneService.planAirPlaneDelete(planAirPlaneDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanAirPlaneDto setTravelId(Long travelId, User user, PlanAirPlaneDto planAirPlaneDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setId(travelId);

        planAirPlaneDto.setTravel(travel);
        planAirPlaneDto.setUser(user);
        return planAirPlaneDto;
    }
}