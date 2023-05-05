package com.travelProject.travelDiary.repository.plan.rtb;

import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanAirplaneThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtbPlanAirplaneThumbNailRepository extends JpaRepository<RtbPlanAirplaneThumbNail, Long> {
    List<RtbPlanAirplaneThumbNail> findAllByPlanAirPlane_Id(Long id);

    List<RtbPlanAirplaneThumbNail> findAllByThId_Id(Long thumbNailId);

    List<RtbPlanAirplaneThumbNail> findAllByThId_IdAndPlanAirPlane_User_Id(Long thumbNailId, String userId);
}