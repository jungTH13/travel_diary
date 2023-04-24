package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.dto.TravelDto;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.PlanService;
import com.travelProject.travelDiary.service.TravelCountryService;
import com.travelProject.travelDiary.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TravelController {
    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelCountryService travelCountryService;

    @Autowired
    private PlanService planService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping ("/travel/userTravelOne")
    public ResponseBody  getTravelOne(HttpServletRequest request, @RequestBody TravelDto travelDto){
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        Travel travelOne = travelService.selectPlanTravelOne(user.getId(), travelDto.getId());
        String[] travelCountryList = travelCountryService.travelCountrySelect(travelDto.getId());
        List<Map<String,Object>> planList = planService.getUserPlan(user.getId(), travelDto.getId());

        Date minDate = null;
        Date maxDate = null;

        if(planList.size() > 0) {
            minDate = (Date) planList.get(0).getOrDefault("orderDate", "");
            maxDate = (Date) planList.get(planList.size() - 1).getOrDefault("orderDate", "");
        }

        result.put("travelOne", travelOne);
        result.put("minDate", minDate == null ? "" : minDate);
        result.put("maxDate", maxDate == null ? "" : maxDate);
        result.put("travelCountryList", travelCountryList);

        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @GetMapping("/travel/userTravelList")
    public ResponseBody  getTravelList(HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> selectPlanTravelList = travelService.selectPlanTravelList(user.getId());
        List<Map<String, Object>> selectEndTravelList = travelService.selectEndTravelList(user.getId());

        result.put("planTravel", selectPlanTravelList);
        result.put("endTravel", selectEndTravelList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/travelInsert")
    public ResponseBody setTravelInsert(HttpServletRequest request, @RequestBody TravelDto travelDto) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) request.getAttribute("user");

        Travel travel = modelMapper.map(travelDto, Travel.class);
        String[] countryArr = travelDto.getCountry();
        travel.setUser(user);

        Long travelId = travelService.travelInsert(travel, countryArr);
        result.put("travelId", travelId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/travelUpdate")
    public ResponseBody setTravelUpdate(HttpServletRequest request, @RequestBody TravelDto travelDto) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) request.getAttribute("user");

        Travel travel = modelMapper.map(travelDto, Travel.class);
        setTravelId(travel.getId(), user);
        String[] countryArr = travelDto.getCountry();
        travel.setUser(user);

        Long travelId = travelService.travelUpdate(travel, countryArr);
        result.put("travelId", travelId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/travelDelete/{travelId}")
    public ResponseBody setTravelDelete(HttpServletRequest request, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");
        Travel travel = setTravelId(travelId, user);
        
        travelService.travelDelete(travel);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public Travel setTravelId(Long travelId, User user) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setUser(user);
        travel.setId(travelId);
        return travel;
    }
}