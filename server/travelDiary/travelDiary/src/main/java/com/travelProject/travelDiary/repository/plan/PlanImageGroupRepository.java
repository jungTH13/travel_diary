package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanImageGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanImageGroupRepository extends JpaRepository<PlanImageGroup, Long> {

    PlanImageGroup findByIdAndUser_Id(Long t_id,String id);

    List<PlanImageGroup> findAllByTravel_IdAndUser_Id(Long travelId, String id);

    List<PlanImageGroup> findAllByTravel_IdAndUser_IdOrderByDateAsc(Long travelId, String userId);


}