package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.Impl.PlanCommonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PlanController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private PlanCommonServiceImpl planCommonService;

    @SuppressWarnings("unchecked")
    @GetMapping("/travel/{travelId}/plan/userPlaneList")
    public ResponseBody getUserPlaneList (HttpServletRequest request, @PathVariable Long travelId){
        User user = (User) request.getAttribute("user");

        Travel resultTravel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(resultTravel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        Map<String,Object> results = new HashMap<>();
        List<Map<String, Object>> planList = planCommonService.getUserPlanList(user.getId(), travelId);
        List<PlanAccountBook> planAccountBookList = planCommonService.getUserPlanAccountBookList(user.getId(), travelId);
        List<PlanCheckListTitle> planCheckListTitle = planCommonService.getUserPlanCheckListTitleList(user.getId(), travelId);

        Map<String, Object> userDataMap = planCommonService.getUserData(planList, planAccountBookList, planCheckListTitle);
        results.put("planAccountBookList", userDataMap.get("planAccountBookList"));

        List<Map<String, Object>> planUserList = (List<Map<String, Object>>) userDataMap.get("planList");
        results.put("planList", planCommonService.getUserThumbNailList((List<Map<String, Object>>) userDataMap.get("planList")));
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
