package com.travelProject.travelDiary.repository.plan.rtb;

import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanHotelThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtbPlanHotelThumbNailRepository extends JpaRepository<RtbPlanHotelThumbNail, Long> {
    List<RtbPlanHotelThumbNail> findAllByPlanHotel_Id(Long id);

    List<RtbPlanHotelThumbNail> findAllByThId_Id(Long thumbNailId);
}