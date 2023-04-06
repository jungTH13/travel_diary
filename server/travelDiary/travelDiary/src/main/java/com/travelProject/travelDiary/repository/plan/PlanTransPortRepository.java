package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanTransPort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanTransPortRepository extends JpaRepository<PlanTransPort, Long> {

    PlanTransPort findByIdAndUser_Id(Long t_id,String id);

    List<PlanTransPort> findAllByTravel_IdAndUser_Id(Long travelId, String userId);
}