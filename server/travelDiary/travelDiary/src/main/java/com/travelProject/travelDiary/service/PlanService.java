package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.Plan;
import com.travelProject.travelDiary.interfaces.mapping.PlanMapping;
import com.travelProject.travelDiary.repository.PlanRepository;
import com.travelProject.travelDiary.service.abstractions.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<PlanMapping> getUserPlan(String id){
        List<PlanMapping> planList = planRepository.findByUser_Id(id);

        return planList;
    }
}
