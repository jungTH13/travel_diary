package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanRestaurantDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanRestaurant;
import com.travelProject.travelDiary.service.ThumbnailService;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanRestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanRestaurantController {
    @Autowired
    private PlanRestaurantService planRestaurantService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/{travelId}/plan/restaurant/restaurantOne")
    public ResponseBody  getPlanRestaurantUpdateOne(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planRestaurantDto);
        PlanRestaurant planRestaurantOne= planRestaurantService.selectPlanRestaurantOne(planRestaurantDto, user.getId());
        List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pr", planRestaurantOne.getId());

        result.put("planRestaurantOne", planRestaurantOne);
        result.put("thumbNailList", thumbNailList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/restaurant/restaurantList")
    public ResponseBody  getPlanRestaurantList(HttpServletRequest request, @RequestBody(required = false) PlanRestaurantDto planRestaurantDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planRestaurantDto);
        List<PlanRestaurant> planRestaurantList = planRestaurantService.selectPlanRestaurantList(planRestaurantDto, user.getId());

        result.put("planRestaurantList", planRestaurantList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/restaurant/restaurantInsert")
    public ResponseBody setPlanRestaurantInsert(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planRestaurantDto);
        Long planRestaurantId = planRestaurantService.planRestaurantInsert(planRestaurantDto);
        result.put("planRestaurantId", planRestaurantId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/restaurant/restaurantUpdate")
    public ResponseBody setPlanRestaurantUpdate(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planRestaurantDto);
        Long planRestaurantId = planRestaurantService.planRestaurantUpdate(planRestaurantDto);
        result.put("planRestaurantId", planRestaurantId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/restaurant/restaurantDelete/{planId}")
    public ResponseBody setPlanRestaurantDelete(HttpServletRequest request
            , @RequestBody PlanRestaurantDto planRestaurantDto
            , @PathVariable Long travelId
            , @PathVariable Long planId) {
        User user = (User) request.getAttribute("user");

        planRestaurantDto.setId(planId);
        setTravelId(travelId, user, planRestaurantDto);
        planRestaurantService.planRestaurantDelete(planRestaurantDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanRestaurantDto setTravelId(Long travelId, User user, PlanRestaurantDto planRestaurantDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setId(travelId);

        planRestaurantDto.setTravel(travel);
        planRestaurantDto.setUser(user);
        return planRestaurantDto;
    }
}