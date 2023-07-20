package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanCheckListTitleDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanCheckListDetail;
import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanCheckListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanCheckListController {
    @Autowired
    private PlanCheckListService planCheckListService;

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/{travelId}/plan/checkList/checkListOne")
    public ResponseBody  getPlanCheckListOne(HttpServletRequest request, @RequestBody(required = false) PlanCheckListTitleDto planCheckListTitleDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planCheckListTitleDto);
        PlanCheckListTitle planCheckListTitle = planCheckListService.selectPlanCheckListTitleOne(planCheckListTitleDto, user.getId());
        List<PlanCheckListDetail> planCheckListDetailList = planCheckListService.selectPlanCheckListDetailList(planCheckListTitleDto.getId());

        result.put("planCheckListTitle", planCheckListTitle);
        result.put("planCheckListDetailList", planCheckListDetailList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/checkList/checkListList")
    public ResponseBody  getPlanCheckListList(HttpServletRequest request, @RequestBody(required = false) PlanCheckListTitleDto planCheckListTitleDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planCheckListTitleDto);
        List<Map<String, Object>> planCheckListTitleList = planCheckListService.selectPlanCheckListTitleList(planCheckListTitleDto, user.getId());

        result.put("planCheckListTitleList", planCheckListTitleList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/checkList/checkListInsert")
    public ResponseBody setPlanCheckListInsert(HttpServletRequest request, @RequestBody PlanCheckListTitleDto planCheckListTitleDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planCheckListTitleDto);
        Long planAccountBookId = planCheckListService.planCheckListInsert(planCheckListTitleDto);
        result.put("planAccountBookId", planAccountBookId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/checkList/checkListUpdate")
    public ResponseBody setPlanCheckListUpdate(HttpServletRequest request, @RequestBody PlanCheckListTitleDto planCheckListTitleDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planCheckListTitleDto);
        Long planCheckListTitleId = planCheckListService.planCheckListUpdate(planCheckListTitleDto);
        result.put("planCheckListTitleId", planCheckListTitleId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/checkList/checkListDelete/{planId}")
    public ResponseBody setPlanCheckListDelete(HttpServletRequest request
            , @RequestBody PlanCheckListTitleDto planCheckListTitleDto
            , @PathVariable Long travelId
            , @PathVariable Long planId) {
        User user = (User) request.getAttribute("user");

        planCheckListTitleDto.setId(planId);
        setTravelId(travelId, user, planCheckListTitleDto);
        planCheckListService.planCheckListTitleDelete(planCheckListTitleDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanCheckListTitleDto setTravelId(Long travelId, User user, PlanCheckListTitleDto planCheckListTitleDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setId(travelId);

        planCheckListTitleDto.setTravel(travel);
        planCheckListTitleDto.setUser(user);
        return planCheckListTitleDto;
    }
}