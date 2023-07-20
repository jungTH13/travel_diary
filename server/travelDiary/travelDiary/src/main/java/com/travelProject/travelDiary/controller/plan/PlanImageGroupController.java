package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanImageGroupDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanImageGroup;
import com.travelProject.travelDiary.service.ThumbnailService;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanImageGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanImageGroupController {
    @Autowired
    private PlanImageGroupService planImageGroupService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/{travelId}/plan/imageGroup/imageGroupOne")
    public ResponseBody getPlanImageGroupOne(HttpServletRequest request, @RequestBody PlanImageGroupDto planImageGroupDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planImageGroupDto);
        PlanImageGroup planImageGroupOne = planImageGroupService.selectPlanImageGroupOne(planImageGroupDto, user.getId());
        List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pig", planImageGroupOne.getId());

        result.put("planImageGroupOne", planImageGroupOne);
        result.put("thumbNailList", thumbNailList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/imageGroup/imageGroupList")
    public ResponseBody  getPlanEtcList(HttpServletRequest request, @RequestBody(required = false) PlanImageGroupDto PlanImageGroupDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, PlanImageGroupDto);

        List<Map<String, Object>> resulList = new ArrayList<Map<String, Object>>();
        List<PlanImageGroup> planImageGroupList = planImageGroupService.selectPlanImageGroupList(PlanImageGroupDto, user.getId());
        for(PlanImageGroup param : planImageGroupList) {
            Map<String, Object> insertParam = new HashMap<String, Object>();
            List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pig", param.getId());

            insertParam = param.toMap();
            insertParam.put("thumbNailList", thumbNailList);
            resulList.add(insertParam);
        }

        result.put("planImageGroupList", resulList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/imageGroup/imageGroupInsert")
    public ResponseBody setPlanImageGroupInsert(HttpServletRequest request, @RequestBody PlanImageGroupDto planImageGroupDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planImageGroupDto);
        Long planImageGroupId = planImageGroupService.planImageGroupInsert(planImageGroupDto);
        result.put("planImageGroupId", planImageGroupId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/imageGroup/imageGroupUpdate")
    public ResponseBody setPlanImageGroupUpdate(HttpServletRequest request, @RequestBody PlanImageGroupDto planImageGroupDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, planImageGroupDto);
        Long planImageGroupId = planImageGroupService.planImageGroupUpdate(planImageGroupDto);
        result.put("planImageGroupId", planImageGroupId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/imageGroup/imageGroupDelete/{planId}")
    public ResponseBody setPlanImageGroupDelete(HttpServletRequest request
            , @RequestBody PlanImageGroupDto planImageGroupDto
            , @PathVariable Long travelId
            , @PathVariable Long planId) {
        User user = (User) request.getAttribute("user");

        planImageGroupDto.setId(planId);
        setTravelId(travelId, user, planImageGroupDto);
        planImageGroupService.planImageGroupDelete(planImageGroupDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanImageGroupDto setTravelId(Long travelId, User user, PlanImageGroupDto planImageGroupDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }
        Travel travel = new Travel();
        travel.setId(travelId);

        planImageGroupDto.setTravel(travel);
        planImageGroupDto.setUser(user);
        return planImageGroupDto;
    }
}