package com.travelProject.travelDiary.repository.plan.rtb;

import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanTransportThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtbPlanTransportThumbNailRepository extends JpaRepository<RtbPlanTransportThumbNail, Long> {
    List<RtbPlanTransportThumbNail> findAllByPlanTransPort_Id(Long planTypeId);

    List<RtbPlanTransportThumbNail> findAllByThId_Id(Long thumbNailId);
}