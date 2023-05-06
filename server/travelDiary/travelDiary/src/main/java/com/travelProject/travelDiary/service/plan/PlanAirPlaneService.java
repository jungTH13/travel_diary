package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanAirPlaneDto;
import com.travelProject.travelDiary.entity.plan.PlanAirPlane;
import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanAirplaneThumbNail;
import com.travelProject.travelDiary.repository.plan.PlanAirPlaneRepository;
import com.travelProject.travelDiary.repository.plan.rtb.RtbPlanAirplaneThumbNailRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanAirPlaneService {
    @Autowired
    private PlanAirPlaneRepository planAirPlaneRepository;

    @Autowired
    private RtbPlanAirplaneThumbNailRepository rtbPlanAirplaneThumbNailRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanAirPlane selectPlanAirPlaneOne(PlanAirPlaneDto planAirPlaneDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long hotelId = planAirPlaneDto.getId();
        PlanAirPlane planAirPlaneOne = planAirPlaneRepository.findByIdAndUser_Id(hotelId, userId);
        return planAirPlaneOne;
    }

    public List<PlanAirPlane> selectPlanAirPlaneList(PlanAirPlaneDto planAirPlaneDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = planAirPlaneDto.getTravel().getId();
        List<PlanAirPlane> PlanAirPlaneList = planAirPlaneRepository.findAllByTravel_IdAndUser_IdOrderByDepartDateAsc(travelId, userId);
        return PlanAirPlaneList;
    }

    public Long planAirPlaneInsert(PlanAirPlaneDto planAirPlaneDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planAirPlaneDto.getId();
        Long travelId = planAirPlaneDto.getTravel().getId();
        PlanAirPlane planAirPlane = modelMapper.map(planAirPlaneDto, PlanAirPlane.class);
        planAirPlane.setCreatedDate(time);
        planAirPlane.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planAirPlaneId = planAirPlaneRepository.save(planAirPlane).getId();
            return planAirPlaneId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planAirPlaneUpdate(PlanAirPlaneDto planAirPlaneDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanAirPlane planAirPlane = modelMapper.map(planAirPlaneDto, PlanAirPlane.class);
        planAirPlane.setModifiedDate(time);
        Long id = planAirPlane.getId();
        Long travelId = planAirPlaneDto.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanAirPlane planAirPlane2 = planAirPlaneRepository.findByIdAndUser_Id(id, planAirPlane.getUser().getId());
        if(!planAirPlane.getUser().getId().equals(planAirPlane2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planAirPlaneId = planAirPlaneRepository.save(planAirPlane).getId();
        return planAirPlaneId;
    }

    public void planAirPlaneDelete(PlanAirPlaneDto planAirPlaneDto) {
        PlanAirPlane planAirPlane = modelMapper.map(planAirPlaneDto, PlanAirPlane.class);
        Long id = planAirPlane.getId();

        PlanAirPlane planAirPlane2 = planAirPlaneRepository.findByIdAndUser_Id(id, planAirPlane.getUser().getId());
        if(!planAirPlane.getUser().getId().equals(planAirPlane2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        List<RtbPlanAirplaneThumbNail> rtbPlanAirplaneThumbNailList = rtbPlanAirplaneThumbNailRepository.findAllByPlanAirPlane_Id(id);
        for(RtbPlanAirplaneThumbNail deleteParam :rtbPlanAirplaneThumbNailList) {
            rtbPlanAirplaneThumbNailRepository.deleteById(deleteParam.getId());
        }

        planAirPlaneRepository.delete(planAirPlane);
    }
}