package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanTransPortDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanTransPort;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanTransPortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanTransPortController {
    @Autowired
    private PlanTransPortService planTransPortService;

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/plan/transPort/transPortOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanTransPortDto planTransPortDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planTransPortDto);
        PlanTransPort planTransPortOne= planTransPortService.selectPlanTransPortOne(planTransPortDto, user.getId());

        result.put("planTransPortOne", planTransPortOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/transPort/transPortList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody(required = false) PlanTransPortDto planTransPortDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planTransPortDto);
        List<PlanTransPort> planTransPortList = planTransPortService.selectPlanTransPortList(planTransPortDto, user.getId());

        result.put("planTransPortList", planTransPortList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/transPort/transPortInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanTransPortDto planTransPortDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planTransPortDto);
        Long planTransPortId = planTransPortService.planTransPortInsert(planTransPortDto);
        result.put("planTransPortId", planTransPortId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/plan/transPort/transPortUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanTransPortDto planTransPortDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planTransPortDto);
        Long planTransPortId = planTransPortService.planTransPortUpdate(planTransPortDto);
        result.put("planTransPortId", planTransPortId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/plan/transPort/transPortDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanTransPortDto planTransPortDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        setTravelId(travelId, user, planTransPortDto);
        planTransPortService.planTransPortDelete(planTransPortDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanTransPortDto setTravelId(Long travelId, User user, PlanTransPortDto planTransPortDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setId(travelId);

        planTransPortDto.setTravel(travel);
        planTransPortDto.setUser(user);
        return planTransPortDto;
    }
}