package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Plan;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/userPlanList")
    public ResponseBody getDefaultFormPlanList (HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        List planList = planService.getUserPlan(user.getId());

        return ResponseBody.builder().code(200).msg("success").results(planList).build();
    }


}
