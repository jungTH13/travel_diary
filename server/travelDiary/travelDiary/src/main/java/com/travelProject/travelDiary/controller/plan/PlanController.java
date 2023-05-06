package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class PlanController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private PlanCommonService planCommonService;

    @GetMapping("/travel/{travelId}/plan/userPlaneList")
    public ResponseBody getUserPlaneList (HttpServletRequest request, @PathVariable Long travelId){
        User user = (User) request.getAttribute("user");

        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Map<String,Object> results = new HashMap<>();
        Map<String, Object> planList = planCommonService.getUserPlan(user.getId(), travelId);

        results.put("planList", planList.get("planList"));
        results.put("planAccountBookList", planList.get("planAccountBookList"));
        return ResponseBody.builder().code(200).msg("success").results(results).build();
    }

    @PutMapping("/travel/{travelId}/plan/memoUpdate")
    public ResponseBody setPlanMemoUpdate(HttpServletRequest request, @RequestBody Map<String, Object> param, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        travelIdCheck(travelId, user);
        Long planId = planCommonService.planMemoUpdate(travelId, user.getId(), param);
        result.put("planId", planId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    public void travelIdCheck(Long travelId, User user) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }
    }
}
