package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanTransPortDto;
import com.travelProject.travelDiary.entity.plan.PlanTransPort;
import com.travelProject.travelDiary.repository.plan.PlanTransPortRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanTransPortService {
    @Autowired
    private PlanTransPortRepository planTransPortRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanTransPort selectPlanTransPortOne(PlanTransPortDto planTransPortDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long hotelId = planTransPortDto.getId();
        PlanTransPort planTransPortOne = planTransPortRepository.findByIdAndUser_Id(hotelId, userId);
        return planTransPortOne;
    }

    public List<PlanTransPort> selectPlanTransPortList(PlanTransPortDto planTransPortDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = planTransPortDto.getTravel().getId();
        List<PlanTransPort> planTransPortList = planTransPortRepository.findAllByTravel_IdAndUser_Id(travelId, userId);
        return planTransPortList;
    }

    public Long planTransPortInsert(PlanTransPortDto planTransPortDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planTransPortDto.getId();
        Long travelId = planTransPortDto.getTravel().getId();
        PlanTransPort planTransPort = modelMapper.map(planTransPortDto, PlanTransPort.class);
        planTransPort.setCreatedDate(time);
        planTransPort.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planTransPortId = planTransPortRepository.save(planTransPort).getId();
            return planTransPortId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planTransPortUpdate(PlanTransPortDto planTransPortDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanTransPort planTransPort = modelMapper.map(planTransPortDto, PlanTransPort.class);
        planTransPort.setModifiedDate(time);
        Long id = planTransPort.getId();
        Long travelId = planTransPortDto.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanTransPort PlanTransPort2 = planTransPortRepository.findByIdAndUser_Id(id, planTransPort.getUser().getId());
        if(!planTransPort.getUser().getId().equals(PlanTransPort2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planTransPortId = planTransPortRepository.save(planTransPort).getId();
        return planTransPortId;
    }

    public void planTransPortDelete(PlanTransPortDto planTransPortDto) {
        PlanTransPort planTransPort = modelMapper.map(planTransPortDto, PlanTransPort.class);
        Long id = planTransPort.getId();

        PlanTransPort planTransPort2 = planTransPortRepository.findByIdAndUser_Id(id, planTransPort.getUser().getId());
        if(!planTransPort.getUser().getId().equals(planTransPort2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        planTransPortRepository.delete(planTransPort);
    }
}