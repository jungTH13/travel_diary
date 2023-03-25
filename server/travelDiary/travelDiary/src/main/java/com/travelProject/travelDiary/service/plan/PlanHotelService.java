package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.PlanHotelDto;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import com.travelProject.travelDiary.repository.plan.PlanHotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.travelProject.travelDiary.dto.ErrorCode.INVALID_USER_PARAMETER;
import static com.travelProject.travelDiary.dto.ErrorCode.INVALID_PARAMETER;
import static com.travelProject.travelDiary.dto.ErrorCode.DIFFERENT_USER_PARAMETER;

@Service
public class PlanHotelService {
    @Autowired
    private PlanHotelRepository planHotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanHotelService() {
    }

    public List<PlanHotel> selectPlanHotelList(PlanHotelDto PlanHotelDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(INVALID_USER_PARAMETER);
        }

        Long travelId = PlanHotelDto.getTravel().getId();
        List<PlanHotel> planHotelResult = planHotelRepository.findAllByTravel_IdAndUser_Id(travelId, userId);
        return planHotelResult;
    }

    public void planHotelInsert(PlanHotelDto planHotelDto) {
        LocalDateTime time = LocalDateTime.now();
        planHotelDto.setCreatedDate(time);
        planHotelDto.setModifiedDate(time);

        Long id = planHotelDto.getId();
        PlanHotel planHotel = modelMapper.map(planHotelDto, PlanHotel.class);
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