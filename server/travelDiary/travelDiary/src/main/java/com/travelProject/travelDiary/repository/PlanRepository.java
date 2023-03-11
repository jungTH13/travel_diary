package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Plan;
import com.travelProject.travelDiary.interfaces.mapping.PlanMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan,Long> {

    List<PlanMapping> findByUser_Id(String Id);
}
