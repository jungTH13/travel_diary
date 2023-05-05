package com.travelProject.travelDiary.repository.plan.rtb;

import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanRestaurantThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtbPlanRestaurantThumbNailRepository extends JpaRepository<RtbPlanRestaurantThumbNail, Long> {
    List<RtbPlanRestaurantThumbNail> findAllByPlanRestaurant_Id(Long planTypeId);

    List<RtbPlanRestaurantThumbNail> findAllByThId_Id(Long thumbNailId);
}