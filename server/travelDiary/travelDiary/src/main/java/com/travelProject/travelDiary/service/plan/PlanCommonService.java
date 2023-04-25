package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.plan.*;
import com.travelProject.travelDiary.repository.plan.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PlanCommonService {
    @Autowired
    private PlanAirPlaneRepository planAirPlaneRepository;

    @Autowired
    private PlanEtcRepository planEtcRepository;

    @Autowired
    private PlanHotelRepository planHotelRepository;

    @Autowired
    private PlanRestaurantRepository planRestaurantRepository;

    @Autowired
    private PlanTransPortRepository planTransPortRepository;

    public Long planMemoUpdate(Long travelId, String userId, Map<String, Object> param) {
        LocalDateTime time = LocalDateTime.now();
        String memo = (String) param.getOrDefault("memo","");
        String planType = (String) param.getOrDefault("planType","");
        Long planId = Long.parseLong(param.getOrDefault("planId","0").toString());

        LocalDateTime localDateTime = LocalDateTime.now();

        Long resultPlanId = 0L;

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if((planId <= 0 || planId == null)
            || !(planType.equals("pa") || planType.equals("pe") || planType.equals("ph") || planType.equals("pr") || planType.equals("pt"))
        ) {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }

        if(planType.equals("pa")) {
            PlanAirPlane planAirPlane = planAirPlaneRepository.findByIdAndUser_Id(planId, userId);

            if(planAirPlane == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planAirPlane.setMemo(memo);
            planAirPlane.setModifiedDate(time);
            resultPlanId = planAirPlaneRepository.save(planAirPlane).getId();
        } else if(planType.equals("pe")) {
            PlanEtc planEtc = planEtcRepository.findByIdAndUser_Id(planId, userId);

            if(planEtc == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planEtc.setMemo(memo);
            planEtc.setModifiedDate(time);
            resultPlanId = planEtcRepository.save(planEtc).getId();
        } else if(planType.equals("ph")) {
            PlanHotel planHotel = planHotelRepository.findByIdAndUser_Id(planId, userId);

            if(planHotel == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planHotel.setMemo(memo);
            planHotel.setModifiedDate(time);
            resultPlanId = planHotelRepository.save(planHotel).getId();
        } else if(planType.equals("pr")) {
            PlanRestaurant planRestaurant = planRestaurantRepository.findByIdAndUser_Id(planId, userId);

            if(planRestaurant == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planRestaurant.setMemo(memo);
            planRestaurant.setModifiedDate(time);
            resultPlanId = planRestaurantRepository.save(planRestaurant).getId();
        } else if(planType.equals("pt")) {
            PlanTransPort planTransPort = planTransPortRepository.findByIdAndUser_Id(planId, userId);

            if(planTransPort == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planTransPort.setMemo(memo);
            planTransPort.setModifiedDate(time);
            resultPlanId = planTransPortRepository.save(planTransPort).getId();
        }
        return resultPlanId;
    }
}