package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.Plan;
import com.travelProject.travelDiary.interfaces.mapping.PlanMapping;
import com.travelProject.travelDiary.repository.PlanRepository;
import com.travelProject.travelDiary.service.abstractions.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Map<String,Object>> getUserPlan(String id){
        List<Map<String,Object>> planList = planRepository.findByUser_Id(id);

        return planList;
    }
}
