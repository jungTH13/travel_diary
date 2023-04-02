package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanHotelRepository extends JpaRepository<PlanHotel, Long> {

    PlanHotel findByIdAndUser_Id(Long t_id,String id);

    List<PlanHotel> findAllByTravel_IdAndUser_Id(Long travelId, String userId);


}