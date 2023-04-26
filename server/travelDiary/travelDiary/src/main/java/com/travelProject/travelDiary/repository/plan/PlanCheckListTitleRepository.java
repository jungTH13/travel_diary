package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanCheckListTitleRepository extends JpaRepository<PlanCheckListTitle, Long> {

    PlanCheckListTitle findByIdAndUser_Id(Long t_id,String id);
}