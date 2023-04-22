package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanEtcDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanEtc;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanEtcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanEtcController {
    @Autowired
    private PlanEtcService planEtcService;

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/{travelId}/plan/etc/etcOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planEtcDto);
        PlanEtc planEtcOne= planEtcService.selectPlanEtcOne(planEtcDto, user.getId());

        result.put("planEtcOne", planEtcOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/etc/etcList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody(required = false) PlanEtcDto planEtcDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planEtcDto);
        List<PlanEtc> planEtclList = planEtcService.selectPlanEtcList(planEtcDto, user.getId());

        result.put("planEtcList", planEtclList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/etc/etcInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planEtcDto);
        Long planEtcId = planEtcService.planEtcInsert(planEtcDto);
        result.put("planEtcId", planEtcId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/etc/etcUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planEtcDto);
        Long planEtcId = planEtcService.planEtcUpdate(planEtcDto);
        result.put("planEtcId", planEtcId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/etc/etcDelete/{planId}")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request
            , @RequestBody PlanEtcDto planEtcDto
            , @PathVariable Long travelId
            , @PathVariable Long planId) {
        User user = (User) request.getAttribute("user");

        planEtcDto.setId(planId);
        setTravelId(travelId, user, planEtcDto);
        planEtcService.planEtcDelete(planEtcDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanEtcDto setTravelId(Long travelId, User user, PlanEtcDto planEtcDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setId(travelId);

        planEtcDto.setTravel(travel);
        planEtcDto.setUser(user);
        return planEtcDto;
    }
}