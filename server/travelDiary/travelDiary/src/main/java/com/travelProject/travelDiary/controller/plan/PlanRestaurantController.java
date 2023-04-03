package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.PlanRestaurantDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanRestaurant;
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

    @GetMapping("/travel/plan/restaurant/restaurantOne")
    public ResponseBody  getHotelOne(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        PlanRestaurant planRestaurantOne= planRestaurantService.selectPlanRestaurantOne(planRestaurantDto, user.getId());

        result.put("planRestaurantOne", planRestaurantOne);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @GetMapping("/travel/plan/restaurant/restaurantList")
    public ResponseBody  getHotelList(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<PlanRestaurant> planRestaurantlList = planRestaurantService.selectPlanRestaurantList(planRestaurantDto, user.getId());

        result.put("planRestaurantlList", planRestaurantlList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/restaurant/restaurantInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto) {
        User user = (User) request.getAttribute("user");
        planRestaurantDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planRestaurantId = planRestaurantService.planRestaurantInsert(planRestaurantDto);
        result.put("planRestaurantId", planRestaurantId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/plan/restaurant/restaurantUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto) {
        User user = (User) request.getAttribute("user");
        planRestaurantDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planRestaurantId = planRestaurantService.planRestaurantUpdate(planRestaurantDto);
        result.put("planRestaurantId", planRestaurantId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/plan/restaurant/restaurantDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanRestaurantDto planRestaurantDto) {
        User user = (User) request.getAttribute("user");
        planRestaurantDto.setUser(user);

        planRestaurantService.planRestaurantDelete(planRestaurantDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}