package com.travelProject.travelDiary.repository.plan.rtb;

import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanEtcThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtbPlanEtcThumbNailRepository extends JpaRepository<RtbPlanEtcThumbNail, Long> {
    List<RtbPlanEtcThumbNail> findAllByPlanEtc_Id(Long id);

    List<RtbPlanEtcThumbNail> findAllByThId_Id(Long thumbNailId);
}