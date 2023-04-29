package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanCheckListDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanCheckListDetailRepository extends JpaRepository<PlanCheckListDetail, Long> {
    List<PlanCheckListDetail> findAllByPlanCheckListTitle_Id(Long planCheckListTitleId);

    PlanCheckListDetail findByPlanCheckListTitle_IdAndChecked(Long planCheckListTitleId, boolean checked);
}