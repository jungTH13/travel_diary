package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PlanCommonService {

    List<Map<String, Object>> getUserPlanList(String userId, Long travelId);

    public List<PlanAccountBook> getUserPlanAccountBookList(String userId, Long travelId);

    public List<PlanCheckListTitle> getUserPlanCheckListTitleList(String userId, Long travelId);

    public Map<String, Object> getUserData(List<Map<String, Object>> planList, List<PlanAccountBook> planAccountBookList, List<PlanCheckListTitle> planCheckListTitle);

    public List<Map<String, Object>> getUserThumbNailList(List<Map<String, Object>> planList);

    public Long planMemoUpdate(Long travelId, String userId, Map<String, Object> param);

    public void planIdSelect(String userId, String planType, Long planTypeId);

    public void rtbThumbNailInsert(String planType, Long planTypeId, Long thumbNailId);

    public void rtbThumbNailDelete(String planType, Long planTypeId, Long thumbNailId, String userId);
}