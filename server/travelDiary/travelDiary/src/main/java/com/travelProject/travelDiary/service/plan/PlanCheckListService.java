package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanCheckListDetailDto;
import com.travelProject.travelDiary.dto.PlanCheckListTitleDto;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanCheckListDetail;
import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import com.travelProject.travelDiary.repository.plan.PlanCheckListDetailRepository;
import com.travelProject.travelDiary.repository.plan.PlanCheckListTitleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlanCheckListService {
    @Autowired
    private PlanCheckListTitleRepository planCheckListTitleRepository;

    @Autowired
    private PlanCheckListDetailRepository planCheckListDetailRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanCheckListTitle selectPlanCheckListTitleOne(PlanCheckListTitleDto planCheckListTitleDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long planCheckListTitleId = planCheckListTitleDto.getId();
        PlanCheckListTitle planCheckListTitle = planCheckListTitleRepository.findByIdAndUser_Id(planCheckListTitleId, userId);
        if(planCheckListTitle == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        return planCheckListTitle;
    }

    public List<Map<String, Object>> selectPlanCheckListTitleList(PlanCheckListTitleDto planCheckListTitleDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        Long travelId = planCheckListTitleDto.getTravel().getId();
        List<PlanCheckListTitle> planCheckListTitle = planCheckListTitleRepository.findAllByTravel_IdAndUser_Id(travelId, userId);
        for(PlanCheckListTitle param : planCheckListTitle) {
            resultList.add(param.toMap());
        }

        return resultList;
    }

    public List<PlanCheckListDetail> selectPlanCheckListDetailList(Long planCheckListTitleId) {
        List<PlanCheckListDetail> planCheckListDetailList = planCheckListDetailRepository.findAllByPlanCheckListTitle_Id(planCheckListTitleId);
        return planCheckListDetailList;
    }

    public Long planCheckListInsert(PlanCheckListTitleDto planCheckListTitleDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = planCheckListTitleDto.getId();
        Long travelId = planCheckListTitleDto.getTravel().getId();
        PlanCheckListTitle planCheckListTitle = modelMapper.map(planCheckListTitleDto, PlanCheckListTitle.class);
        planCheckListTitle.setCreatedDate(time);
        planCheckListTitle.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planCheckListId = planCheckListTitleRepository.save(planCheckListTitle).getId();
            PlanCheckListDetailDto[] planCheckListDetailDtoList = planCheckListTitleDto.getPlanCheckListDetail();

            PlanCheckListTitle insertTitle = new PlanCheckListTitle();
            insertTitle.setId(planCheckListId);

            for (PlanCheckListDetailDto planCheckListDetailDto: planCheckListDetailDtoList) {
                PlanCheckListDetail planCheckListDetail = modelMapper.map(planCheckListDetailDto, PlanCheckListDetail.class);
                planCheckListDetail.setPlanCheckListTitle(insertTitle);
                planCheckListDetail.setCreatedDate(time);
                planCheckListDetail.setModifiedDate(time);
                planCheckListDetailRepository.save(planCheckListDetail);
            }
            return planCheckListId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planCheckListUpdate(PlanCheckListTitleDto planCheckListTitleDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanCheckListTitle planCheckListTitle = modelMapper.map(planCheckListTitleDto, PlanCheckListTitle.class);
        planCheckListTitle.setModifiedDate(time);
        Long id = planCheckListTitle.getId();
        Long travelId = planCheckListTitle.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanCheckListTitle planCheckListTitle2 = planCheckListTitleRepository.findByIdAndUser_Id(id, planCheckListTitle.getUser().getId());
        if(!planCheckListTitle.getUser().getId().equals(planCheckListTitle2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        PlanCheckListDetailDto[] planCheckListDetailDtoList = planCheckListTitleDto.getPlanCheckListDetail();
        List<PlanCheckListDetail> planCheckListDetailList = planCheckListDetailRepository.findAllByPlanCheckListTitle_Id(id);

        PlanCheckListTitle insertTitle = new PlanCheckListTitle();
        insertTitle.setId(id);

        //Dto 배열에서 해당 아이디가 DB에 있는 ID가 같을 경우에만 수정하고
        //Dto 에 ID가 없을 경우에는 저장을 한다.
        for (PlanCheckListDetailDto planCheckListDetailDto: planCheckListDetailDtoList) {
            Long findId = planCheckListDetailDto.getId();
            Optional<PlanCheckListDetail> result = planCheckListDetailList.stream()
                    .filter(dto -> dto.getId().equals(findId))
                    .findFirst();
            PlanCheckListDetail planCheckListDetail = modelMapper.map(planCheckListDetailDto, PlanCheckListDetail.class);

            if (result.isPresent()) {
                planCheckListDetail.setPlanCheckListTitle(insertTitle);
                planCheckListDetail.setModifiedDate(time);
                planCheckListDetailRepository.save(planCheckListDetail);

                planCheckListDetailList.removeIf(detail -> detail.getId() == planCheckListDetailDto.getId());
            } else {
                if(planCheckListDetailDto.getId() == null){
                    planCheckListDetail.setPlanCheckListTitle(insertTitle);
                    planCheckListDetail.setCreatedDate(time);
                    planCheckListDetail.setModifiedDate(time);
                    planCheckListDetailRepository.save(planCheckListDetail).getId();
                }
            }
        }

        //위에 for 문에서 insert, update 를 하고 난뒤 남은 값들은 삭제를 한다.
        for (PlanCheckListDetail deleteParam: planCheckListDetailList) {
            planCheckListDetailRepository.deleteById(deleteParam.getId());
        }

        List<PlanCheckListDetail> isCompletedCheck = planCheckListDetailRepository.findAllByPlanCheckListTitle_IdAndChecked(id,false);
        if(isCompletedCheck.size() > 0) {
            planCheckListTitle.setIsCompleted(false);
        } else {
            planCheckListTitle.setIsCompleted(true);
        }
        Long planCheckListTitleId = planCheckListTitleRepository.save(planCheckListTitle).getId();
        return planCheckListTitleId;
    }

    public void planCheckListTitleDelete(PlanCheckListTitleDto planCheckListTitleDto) {
        PlanCheckListTitle planCheckListTitle = modelMapper.map(planCheckListTitleDto, PlanCheckListTitle.class);
        Long id = planCheckListTitle.getId();

        PlanCheckListTitle planCheckListTitle2 = planCheckListTitleRepository.findByIdAndUser_Id(id, planCheckListTitle.getUser().getId());
        if(!planCheckListTitle.getUser().getId().equals(planCheckListTitle2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        List<PlanCheckListDetail> planCheckListDetail = planCheckListDetailRepository.findAllByPlanCheckListTitle_Id(id);
        for (PlanCheckListDetail param: planCheckListDetail) {
            planCheckListDetailRepository.deleteById(param.getId());
        }

        planCheckListTitleRepository.delete(planCheckListTitle);
    }

    public void planCheckListAllDelete(Long travelId, User user) {
        List<PlanCheckListTitle> planCheckListTitle = planCheckListTitleRepository.findAllByTravel_IdAndUser_Id(travelId, user.getId());
        for(PlanCheckListTitle deleteTitleParam : planCheckListTitle) {
            PlanCheckListTitleDto planCheckListTitleDto = modelMapper.map(deleteTitleParam, PlanCheckListTitleDto.class);
            this.planCheckListTitleDelete(planCheckListTitleDto);
        }
    }
}