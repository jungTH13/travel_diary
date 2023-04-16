package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.AuthService;
import com.travelProject.travelDiary.service.PlanService;
import com.travelProject.travelDiary.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private TravelService travelService;
    @Autowired
    private AuthService authService;

    @GetMapping("/travel/{travelId}/plan/userPlaneList")
    public ResponseBody getDefaultFormPlanList (HttpServletRequest request, @PathVariable Long travelId){
        User user = (User) request.getAttribute("user");

        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Map<String,Object> results = new HashMap<>();
        List planList = planService.getUserPlan(user.getId(), travelId);

        results.put("planList", planList);
        return ResponseBody.builder().code(200).msg("success").results(results).build();
    }
}
