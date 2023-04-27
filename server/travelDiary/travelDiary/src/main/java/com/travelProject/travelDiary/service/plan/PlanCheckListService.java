package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanCheckListDetailDto;
import com.travelProject.travelDiary.dto.PlanCheckListTitleDto;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.entity.plan.PlanCheckListDetail;
import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import com.travelProject.travelDiary.repository.plan.PlanCheckListDetailRepository;
import com.travelProject.travelDiary.repository.plan.PlanCheckListTitleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<PlanCheckListDetail> selectPlanCheckListDetailList(Long planCheckListTitleId) {
        List<PlanCheckListDetail> planCheckListDetailList = planCheckListDetailRepository.findAllByPlanCheckListTitle_Id(planCheckListTitleId);
        return planCheckListDetailList;
    }
//
//    public Long planAccountBookUpdate(PlanAccountBookDto accountBookDto) {
//        LocalDateTime time = LocalDateTime.now();
//
//        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
//        planAccountBook.setModifiedDate(time);
//        Long id = planAccountBook.getId();
//        Long travelId = accountBookDto.getTravel().getId();
//
//        if(travelId < 0 || travelId == null) {
//            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
//        }
//
//        if(id == null) {
//            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
//        }
//
//        PlanAccountBook planAccountBook2 = planAccountBookRepository.findByIdAndUser_Id(id, planAccountBook.getUser().getId());
//        if(!planAccountBook.getUser().getId().equals(planAccountBook2.getUser().getId())){
//            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
//        }
//
//        Long planAccountBookId = planAccountBookRepository.save(planAccountBook).getId();
//        return planAccountBookId;
//    }
//
//    public void planAccountBookDelete(PlanAccountBookDto accountBookDto) {
//        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
//        Long id = planAccountBook.getId();
//
//        PlanAccountBook planAccountBook2 = planAccountBookRepository.findByIdAndUser_Id(id, accountBookDto.getUser().getId());
//        if(!planAccountBook.getUser().getId().equals(planAccountBook2.getUser().getId())){
//            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
//        }
//
//        planAccountBookRepository.delete(planAccountBook);
//    }
}