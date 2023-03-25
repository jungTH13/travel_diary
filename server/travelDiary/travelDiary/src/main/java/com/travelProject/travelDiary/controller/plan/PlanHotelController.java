package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.PlanHotelDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import com.travelProject.travelDiary.service.plan.PlanHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanHotelController {
    @Autowired
    private PlanHotelService planHotelService;

    @GetMapping("/travel/plan/hotel/hotelList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<PlanHotel> planHotelList = planHotelService.selectPlanHotelList(planHotelDto, user.getId());

        result.put("planHotelList", planHotelList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/hotel/hotelInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto) {
        User user = (User) request.getAttribute("user");
        planHotelDto.setUser(user);

        planHotelService.planHotelInsert(planHotelDto);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").build();
    }

    @PutMapping("/travel/plan/hotel/hotelUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto) {
        User user = (User) request.getAttribute("user");
        planHotelDto.setUser(user);

        planHotelService.planHotelUpdate(planHotelDto);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").build();
    }

    @DeleteMapping("/travel/plan/hotel/hotelDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanHotelDto planHotelDto) {
        User user = (User) request.getAttribute("user");
        planHotelDto.setUser(user);

        planHotelService.planHotelDelete(planHotelDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}