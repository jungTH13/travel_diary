package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanAirPlane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanAirPlaneRepository extends JpaRepository<PlanAirPlane, Long> {

    PlanAirPlane findByIdAndUser_Id(Long t_id, String id);

    List<PlanAirPlane> findAllByTravel_IdAndUser_Id(Long travelId, String userId);
}