package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanRestaurantDto;
import com.travelProject.travelDiary.entity.plan.PlanRestaurant;
import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanRestaurantThumbNail;
import com.travelProject.travelDiary.repository.plan.PlanRestaurantRepository;
import com.travelProject.travelDiary.repository.plan.rtb.RtbPlanRestaurantThumbNailRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanRestaurantService {
    @Autowired
    private PlanRestaurantRepository planRestaurantRepository;

    @Autowired
    private RtbPlanRestaurantThumbNailRepository rtbPlanRestaurantThumbNailRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanRestaurant selectPlanRestaurantOne(PlanRestaurantDto planRestaurantDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long hotelId = planRestaurantDto.getId();
        PlanRestaurant planRestaurantOne = planRestaurantRepository.findByIdAndUser_Id(hotelId, userId);
        return planRestaurantOne;
    }

    public List<PlanRestaurant> selectPlanRestaurantList(PlanRestaurantDto planRestaurantDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = planRestaurantDto.getTravel().getId();
        List<PlanRestaurant> planRestaurantList = planRestaurantRepository.findAllByTravel_IdAndUser_IdOrderByDateAsc(travelId, userId);
        return planRestaurantList;
    }

    public Long planRestaurantInsert(PlanRestaurantDto planRestaurantDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planRestaurantDto.getId();
        Long travelId = planRestaurantDto.getTravel().getId();
        PlanRestaurant planRestaurant = modelMapper.map(planRestaurantDto, PlanRestaurant.class);
        planRestaurant.setCreatedDate(time);
        planRestaurant.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planRestaurantId = planRestaurantRepository.save(planRestaurant).getId();
            return planRestaurantId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planRestaurantUpdate(PlanRestaurantDto planRestaurantDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanRestaurant planRestaurant = modelMapper.map(planRestaurantDto, PlanRestaurant.class);
        planRestaurant.setModifiedDate(time);
        Long id = planRestaurant.getId();
        Long travelId = planRestaurantDto.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanRestaurant PlanRestaurant2 = planRestaurantRepository.findByIdAndUser_Id(id, planRestaurant.getUser().getId());
        if(!planRestaurant.getUser().getId().equals(PlanRestaurant2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planRestaurantId = planRestaurantRepository.save(planRestaurant).getId();
        return planRestaurantId;
    }

    public void planRestaurantDelete(PlanRestaurantDto planRestaurantDto) {
        PlanRestaurant planRestaurant = modelMapper.map(planRestaurantDto, PlanRestaurant.class);
        Long id = planRestaurant.getId();

        PlanRestaurant planRestaurant2 = planRestaurantRepository.findByIdAndUser_Id(id, planRestaurant.getUser().getId());
        if(!planRestaurant.getUser().getId().equals(planRestaurant2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        List<RtbPlanRestaurantThumbNail> rtbPlanRestaurantThumbNailList = rtbPlanRestaurantThumbNailRepository.findAllByPlanRestaurant_Id(id);
        for(RtbPlanRestaurantThumbNail deleteParam :rtbPlanRestaurantThumbNailList) {
            rtbPlanRestaurantThumbNailRepository.deleteById(deleteParam.getId());
        }

        planRestaurantRepository.delete(planRestaurant);
    }
}