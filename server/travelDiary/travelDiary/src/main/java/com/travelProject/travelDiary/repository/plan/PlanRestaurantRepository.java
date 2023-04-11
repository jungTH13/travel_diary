package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRestaurantRepository extends JpaRepository<PlanRestaurant, Long> {

    PlanRestaurant findByIdAndUser_Id(Long t_id,String id);

    List<PlanRestaurant> findAllByTravel_IdAndUser_Id(Long travelId, String userId);

    List<PlanRestaurant> findAllByTravel_IdAndUser_IdOrderByDateAsc(Long travelId, String userId);
}