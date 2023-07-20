package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanEtc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanEtcRepository extends JpaRepository<PlanEtc, Long> {

    PlanEtc findByIdAndUser_Id(Long t_id,String id);

    List<PlanEtc> findAllByTravel_IdAndUser_Id(Long travelId, String userId);

    List<PlanEtc> findAllByTravel_IdAndUser_IdOrderByReservationDateAsc(Long travelId, String userId);
}