package com.travelProject.travelDiary.repository.plan.rtb;

import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanImageGroupThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtbPlanImageGroupThumbNailRepository extends JpaRepository<RtbPlanImageGroupThumbNail, Long> {
    List<RtbPlanImageGroupThumbNail> findAllByPlanImageGroup_Id(Long thumbNailId);
}