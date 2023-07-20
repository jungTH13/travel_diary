package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanAccountBookRepository extends JpaRepository<PlanAccountBook, Long> {

    PlanAccountBook findByIdAndUser_Id(Long t_id,String id);

    List<PlanAccountBook> findAllByTravel_IdAndUser_Id(Long travelId, String userId);

    List<PlanAccountBook> findAllByTravel_IdAndUser_IdOrderByPaymentDateAsc(Long travelId, String userId);
}