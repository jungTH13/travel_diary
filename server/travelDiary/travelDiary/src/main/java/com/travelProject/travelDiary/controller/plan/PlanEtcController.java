package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.PlanEtcDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanEtc;
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

    @GetMapping("/travel/plan/etc/etcOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        PlanEtc planEtcOne= planEtcService.selectPlanEtcOne(planEtcDto, user.getId());

        result.put("planEtcOne", planEtcOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @GetMapping("/travel/plan/etc/etcList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<PlanEtc> planEtclList = planEtcService.selectPlanEtcList(planEtcDto, user.getId());

        result.put("planEtclList", planEtclList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/etc/etcInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto) {
        User user = (User) request.getAttribute("user");
        planEtcDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planEtcId = planEtcService.planEtcInsert(planEtcDto);
        result.put("planEtcId", planEtcId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/plan/etc/etcUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto) {
        User user = (User) request.getAttribute("user");
        planEtcDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planEtcId = planEtcService.planEtcUpdate(planEtcDto);
        result.put("planEtcId", planEtcId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/plan/etc/etcDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanEtcDto planEtcDto) {
        User user = (User) request.getAttribute("user");
        planEtcDto.setUser(user);

        planEtcService.planEtcDelete(planEtcDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}