package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanAccountBookDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanAccountBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanAccountBookController {
    @Autowired
    private PlanAccountBookService planAccountBookService;

    @Autowired
    private TravelService travelService;

    @PostMapping("/travel/{travelId}/plan/accountBook/accountBookOne")
    public ResponseBody  getPlanAccountBookOne(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, accountBookDto);
        PlanAccountBook planAccountBook = planAccountBookService.selectPlanAccountBookOne(accountBookDto, user.getId());

        result.put("accountBook", planAccountBook);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/accountBook/accountBookList")
    public ResponseBody  getPlanAccountBookList(HttpServletRequest request, @RequestBody(required = false) PlanAccountBookDto accountBookDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, accountBookDto);
        List<PlanAccountBook> planAccountBookList = planAccountBookService.selectPlanAccountBookList(accountBookDto, user.getId());

        result.put("accountBookList", planAccountBookList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/{travelId}/plan/accountBook/accountBookInsert")
    public ResponseBody setPlanAccountBookInsert(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, accountBookDto);
        Long planAccountBookId = planAccountBookService.planAccountBookInsert(accountBookDto);
        result.put("planAccountBookId", planAccountBookId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/{travelId}/plan/accountBook/accountBookUpdate")
    public ResponseBody setPlanAccountBookUpdate(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto, @PathVariable Long travelId) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        setTravelId(travelId, user, accountBookDto);
        Long accountBookId = planAccountBookService.planAccountBookUpdate(accountBookDto);
        result.put("accountBookId", accountBookId);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").results(result).build();
    }

    @DeleteMapping("/travel/{travelId}/plan/accountBook/accountBookDelete/{planId}")
    public ResponseBody setPlanAccountBookDelete(HttpServletRequest request
            , @RequestBody PlanAccountBookDto accountBookDto
            , @PathVariable Long travelId
            , @PathVariable Long planId) {
        User user = (User) request.getAttribute("user");

        accountBookDto.setId(planId);
        setTravelId(travelId, user, accountBookDto);
        planAccountBookService.planAccountBookDelete(accountBookDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }

    public PlanAccountBookDto setTravelId(Long travelId, User user, PlanAccountBookDto accountBookDto) {
        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Travel travel = new Travel();
        travel.setId(travelId);

        accountBookDto.setTravel(travel);
        accountBookDto.setUser(user);
        return accountBookDto;
    }
}