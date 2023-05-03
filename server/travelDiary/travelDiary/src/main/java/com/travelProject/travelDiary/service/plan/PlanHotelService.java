package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanHotelDto;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanEtc;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import com.travelProject.travelDiary.repository.plan.PlanHotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanHotelService {
    @Autowired
    private PlanHotelRepository planHotelRepository;

    @Autowired
    private ModelMapper modelMapper;

        public PlanHotel selectPlanHotelOne(PlanHotelDto planHotelDto, String userId) {
            if(userId.equals("") || userId == null) {
                throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
            }

            Long hotelId = planHotelDto.getId();
            PlanHotel planHotelOne = planHotelRepository.findByIdAndUser_Id(hotelId, userId);
            return planHotelOne;
        }

    public List<PlanHotel> selectPlanHotelList(PlanHotelDto PlanHotelDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = PlanHotelDto.getTravel().getId();
        List<PlanHotel> planHotelList = planHotelRepository.findAllByTravel_IdAndUser_IdOrderByCheckinDateAsc(travelId, userId);
        return planHotelList;
    }

    public Long planHotelInsert(PlanHotelDto planHotelDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planHotelDto.getId();
        Long travelId = planHotelDto.getTravel().getId();
        PlanHotel planHotel = modelMapper.map(planHotelDto, PlanHotel.class);
        planHotel.setCreatedDate(time);
        planHotel.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planHotelId = planHotelRepository.save(planHotel).getId();
            return planHotelId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planHotelUpdate(PlanHotelDto planHotelDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanHotel planHotel = modelMapper.map(planHotelDto, PlanHotel.class);
        planHotel.setModifiedDate(time);
        Long id = planHotel.getId();
        Long travelId = planHotelDto.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanHotel planHotel2 = planHotelRepository.findByIdAndUser_Id(id, planHotel.getUser().getId());
        if(!planHotel.getUser().getId().equals(planHotel2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planHotelId = planHotelRepository.save(planHotel).getId();
        return planHotelId;
    }

    public void planHotelDelete(PlanHotelDto planHotelDto) {
        PlanHotel planHotel = modelMapper.map(planHotelDto, PlanHotel.class);
        Long id = planHotel.getId();

        PlanHotel planHotel2 = planHotelRepository.findByIdAndUser_Id(id, planHotel.getUser().getId());
        if(!planHotel.getUser().getId().equals(planHotel2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        planHotelRepository.delete(planHotel);
    }

    public void planHotelDeleteList(Long travelId, User user) {
        List<PlanHotel> planHotelListList = planHotelRepository.findAllByTravel_IdAndUser_Id(travelId, user.getId());
        for(PlanHotel deleteParam : planHotelListList) {
            planHotelRepository.deleteById(deleteParam.getId());
        }
    }
}