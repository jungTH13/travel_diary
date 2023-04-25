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
public class PlanCommonController {
    @Autowired
    private PlanCommonService planCommonService;

    @Autowired
    private TravelService travelService;

    @PutMapping("/travel/{travelId}/plan/memoUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody Map<String, Object> param, @PathVariable Long travelId) {
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