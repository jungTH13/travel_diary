package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import com.travelProject.travelDiary.repository.plan.PlanHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.travelProject.travelDiary.dto.ErrorCode.INVALID_USER_PARAMETER;
import static com.travelProject.travelDiary.dto.ErrorCode.INVALID_PARAMETER;
import static com.travelProject.travelDiary.dto.ErrorCode.DIFFERENT_USER_PARAMETER;

@Service
public class PlanHotelService {
    @Autowired
    private PlanHotelRepository planHotelRepository;

    public List<Map<String, Object>> selectPlanHotelList(String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(INVALID_USER_PARAMETER);
        }

        List<Map<String, Object>> resutlList = planHotelRepository.selectPlanHotelList(userId);
        return resutlList;
    }

    public void planHotelInsert(PlanHotel planHotel) {
        LocalDateTime time = LocalDateTime.now();
        planHotel.setCreatedDate(time);
        planHotel.setModifiedDate(time);

        Long id = planHotel.getId();
        if(id == null) {
            planHotelRepository.save(planHotel);
        } else {
            throw new exceptionCode(INVALID_PARAMETER);
        }
    }

    public void planHotelUpdate(PlanHotel planHotel) {
        LocalDateTime time = LocalDateTime.now();
        planHotel.setModifiedDate(time);

        Long id = planHotel.getId();
        PlanHotel planHotel2 = planHotelRepository.findByIdAndUser_Id(id, planHotel.getUser().getId());
        if(!planHotel.getUser().getId().equals(planHotel2.getUser().getId())){
            throw new exceptionCode(DIFFERENT_USER_PARAMETER);
        }

        if(id > 0 || id != null) {
            planHotelRepository.save(planHotel);
        } else {
            throw new exceptionCode(INVALID_PARAMETER);
        }
    }

    public void planHotelDelete(PlanHotel planHotel) {
        Long id = planHotel.getId();
        PlanHotel planHotel2 = planHotelRepository.findByIdAndUser_Id(id, planHotel.getUser().getId());
        if(!planHotel.getUser().getId().equals(planHotel2.getUser().getId())){
            throw new exceptionCode(DIFFERENT_USER_PARAMETER);
        }

        planHotelRepository.delete(planHotel);
    }
}