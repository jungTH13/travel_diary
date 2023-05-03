package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanEtcDto;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.entity.plan.PlanEtc;
import com.travelProject.travelDiary.repository.plan.PlanEtcRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanEtcService {
    @Autowired
    private PlanEtcRepository planEtcRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanEtc selectPlanEtcOne(PlanEtcDto planEtcDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long hotelId = planEtcDto.getId();
        PlanEtc planEtcOne = planEtcRepository.findByIdAndUser_Id(hotelId, userId);
        return planEtcOne;
    }

    public List<PlanEtc> selectPlanEtcList(PlanEtcDto planEtcDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = planEtcDto.getTravel().getId();
        List<PlanEtc> planEtcList = planEtcRepository.findAllByTravel_IdAndUser_IdOrderByReservationDateAsc(travelId, userId);

        return planEtcList;
    }

    public Long planEtcInsert(PlanEtcDto planEtcDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planEtcDto.getId();
        Long travelId = planEtcDto.getTravel().getId();
        PlanEtc planEtc = modelMapper.map(planEtcDto, PlanEtc.class);
        planEtc.setCreatedDate(time);
        planEtc.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planEtcId = planEtcRepository.save(planEtc).getId();
            return planEtcId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planEtcUpdate(PlanEtcDto planEtcDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanEtc planEtc = modelMapper.map(planEtcDto, PlanEtc.class);
        planEtc.setModifiedDate(time);
        Long id = planEtc.getId();
        Long travelId = planEtcDto.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanEtc PlanEtc2 = planEtcRepository.findByIdAndUser_Id(id, planEtc.getUser().getId());
        if(!planEtc.getUser().getId().equals(PlanEtc2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planEtcId = planEtcRepository.save(planEtc).getId();
        return planEtcId;
    }

    public void planEtcDelete(PlanEtcDto planEtcDto) {
        PlanEtc planEtc = modelMapper.map(planEtcDto, PlanEtc.class);
        Long id = planEtc.getId();

        PlanEtc planEtc2 = planEtcRepository.findByIdAndUser_Id(id, planEtc.getUser().getId());
        if(!planEtc.getUser().getId().equals(planEtc2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        planEtcRepository.delete(planEtc);
    }

    public void planEtcDeleteList(Long travelId, User user) {
        List<PlanEtc> planEtcList = planEtcRepository.findAllByTravel_IdAndUser_Id(travelId, user.getId());
        for(PlanEtc deleteParam : planEtcList) {
            planEtcRepository.deleteById(deleteParam.getId());
        }
    }
}