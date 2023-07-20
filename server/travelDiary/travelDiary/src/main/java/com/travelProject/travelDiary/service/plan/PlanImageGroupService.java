package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanImageGroupDto;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanImageGroup;
import com.travelProject.travelDiary.entity.plan.rtb.RtbPlanImageGroupThumbNail;
import com.travelProject.travelDiary.repository.plan.PlanImageGroupRepository;
import com.travelProject.travelDiary.repository.plan.rtb.RtbPlanImageGroupThumbNailRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanImageGroupService {
    @Autowired
    private PlanImageGroupRepository planImageGroupRepository;

    @Autowired
    private RtbPlanImageGroupThumbNailRepository rtbPlanImageGroupThumbNailRepository;

    @Autowired
    private ModelMapper modelMapper;


    public PlanImageGroup selectPlanImageGroupOne(PlanImageGroupDto planImageGroupDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long imageGroupId = planImageGroupDto.getId();
        PlanImageGroup planImageGroupOne = planImageGroupRepository.findByIdAndUser_Id(imageGroupId, userId);
        return planImageGroupOne;
    }

    public List<PlanImageGroup> selectPlanImageGroupList(PlanImageGroupDto planImageGroupDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = planImageGroupDto.getTravel().getId();
        List<PlanImageGroup> planImageGroupList = planImageGroupRepository.findAllByTravel_IdAndUser_IdOrderByDateAsc(travelId, userId);
        return planImageGroupList;
    }


    public Long planImageGroupInsert(PlanImageGroupDto planImageGroupDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planImageGroupDto.getId();
        Long travelId = planImageGroupDto.getTravel().getId();
        PlanImageGroup planImageGroup = modelMapper.map(planImageGroupDto, PlanImageGroup.class);
        planImageGroup.setCreatedDate(time);
        planImageGroup.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planImageGroupId = planImageGroupRepository.save(planImageGroup).getId();
            return planImageGroupId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planImageGroupUpdate(PlanImageGroupDto planImageGroupDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanImageGroup planImageGroup = modelMapper.map(planImageGroupDto, PlanImageGroup.class);
        planImageGroup.setModifiedDate(time);
        Long id = planImageGroup.getId();
        Long travelId = planImageGroup.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanImageGroup planImageGroup2 = planImageGroupRepository.findByIdAndUser_Id(id, planImageGroup.getUser().getId());
        if(!planImageGroup.getUser().getId().equals(planImageGroup2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planImageGroupId = planImageGroupRepository.save(planImageGroup).getId();
        return planImageGroupId;
    }

    public void planImageGroupDelete(PlanImageGroupDto planImageGroupDto) {
        PlanImageGroup planImageGroup = modelMapper.map(planImageGroupDto, PlanImageGroup.class);
        Long id = planImageGroup.getId();

        PlanImageGroup planImageGroup2 = planImageGroupRepository.findByIdAndUser_Id(id, planImageGroup.getUser().getId());
        if(!planImageGroup.getUser().getId().equals(planImageGroup2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        List<RtbPlanImageGroupThumbNail> rtbPlanImageGroupThumbNailList = rtbPlanImageGroupThumbNailRepository.findAllByPlanImageGroup_Id(id);
        for(RtbPlanImageGroupThumbNail deleteParam :rtbPlanImageGroupThumbNailList) {
            rtbPlanImageGroupThumbNailRepository.deleteById(deleteParam.getId());
        }

        planImageGroupRepository.delete(planImageGroup);
    }

    public void planImageGroupDeleteList(Long travelId, User user) {
        List<PlanImageGroup> planImageGroupList = planImageGroupRepository.findAllByTravel_IdAndUser_Id(travelId, user.getId());
        for(PlanImageGroup deleteParam : planImageGroupList) {
            List<RtbPlanImageGroupThumbNail> rtbPlanImageGroupThumbNailList = rtbPlanImageGroupThumbNailRepository.findAllByPlanImageGroup_Id(deleteParam.getId());
            for(RtbPlanImageGroupThumbNail deleteRtbParam :rtbPlanImageGroupThumbNailList) {
                rtbPlanImageGroupThumbNailRepository.deleteById(deleteRtbParam.getId());
            }

            planImageGroupRepository.deleteById(deleteParam.getId());
        }
    }
}