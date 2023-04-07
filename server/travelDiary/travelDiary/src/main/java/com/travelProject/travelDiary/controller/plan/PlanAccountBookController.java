package com.travelProject.travelDiary.controller.plan;

import com.travelProject.travelDiary.dto.PlanAccountBookDto;
import com.travelProject.travelDiary.dto.ResponseBody;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
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

    @PostMapping("/travel/plan/accountBook/accountBookOne")
    public ResponseBody  getAccountBookOne(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        PlanAccountBook planAccountBook = planAccountBookService.selectPlanAccountBookOne(accountBookDto, user.getId());

        result.put("accountBook", planAccountBook);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/accountBook/accountBookList")
    public ResponseBody  getAccountBookList(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto) {
        User user = (User) request.getAttribute("user");

        Map<String, Object> result = new HashMap<>();
        List<PlanAccountBook> planAccountBookList = planAccountBookService.selectPlanAccountBookList(accountBookDto, user.getId());

        result.put("accountBookList", planAccountBookList);
        return ResponseBody.builder().code(200).msg("조회 성공 했습니다.").results(result).build();
    }

    @PostMapping("/travel/plan/accountBook/accountBookInsert")
    public ResponseBody setHotelInsert(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto) {
        User user = (User) request.getAttribute("user");
        accountBookDto.setUser(user);

        Map<String, Object> result = new HashMap<>();
        Long planAccountBookId = planAccountBookService.planAccountBookInsert(accountBookDto);
        result.put("planAccountBookId", planAccountBookId);
        return ResponseBody.builder().code(200).msg("저장 성공 했습니다.").results(result).build();
    }

    @PutMapping("/travel/plan/accountBook/accountBookUpdate")
    public ResponseBody setPlanHotelUpdate(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto) {
        User user = (User) request.getAttribute("user");
        accountBookDto.setUser(user);

        planAccountBookService.planAccountBookUpdate(accountBookDto);
        return ResponseBody.builder().code(200).msg("수정 성공 했습니다.").build();
    }

    @DeleteMapping("/travel/plan/accountBook/accountBookDelete")
    public ResponseBody setPlanHotelDelete(HttpServletRequest request, @RequestBody PlanAccountBookDto accountBookDto) {
        User user = (User) request.getAttribute("user");
        accountBookDto.setUser(user);

        planAccountBookService.planAccountBookDelete(accountBookDto);
        return ResponseBody.builder().code(200).msg("삭제 성공 했습니다.").build();
    }
}