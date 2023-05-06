package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.PlanAccountBookDto;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.repository.plan.PlanAccountBookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class PlanAccountBookService {
    @Autowired
    private PlanAccountBookRepository planAccountBookRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanAccountBook selectPlanAccountBookOne(PlanAccountBookDto accountBookDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long planAccountBookId = accountBookDto.getId();
        PlanAccountBook planAccountBook = planAccountBookRepository.findByIdAndUser_Id(planAccountBookId, userId);
        return planAccountBook;
    }

    public List<PlanAccountBook> selectPlanAccountBookList(PlanAccountBookDto accountBookDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        Long travelId = accountBookDto.getTravel().getId();
        List<PlanAccountBook> planAccountBookList = planAccountBookRepository.findAllByTravel_IdAndUser_IdOrderByPaymentDateAsc(travelId, userId);
        return planAccountBookList;
    }

    public Long planAccountBookInsert(PlanAccountBookDto accountBookDto) {
        LocalDateTime time = LocalDateTime.now();

        Long id = accountBookDto.getId();
        Long travelId = accountBookDto.getTravel().getId();
        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
        planAccountBook.setCreatedDate(time);
        planAccountBook.setModifiedDate(time);

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            Long planAccountBookId = planAccountBookRepository.save(planAccountBook).getId();
            return planAccountBookId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long planAccountBookUpdate(PlanAccountBookDto accountBookDto) {
        LocalDateTime time = LocalDateTime.now();

        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
        planAccountBook.setModifiedDate(time);
        Long id = planAccountBook.getId();
        Long travelId = accountBookDto.getTravel().getId();

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if(id == null) {
            throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
        }

        PlanAccountBook planAccountBook2 = planAccountBookRepository.findByIdAndUser_Id(id, planAccountBook.getUser().getId());
        if(!planAccountBook.getUser().getId().equals(planAccountBook2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        Long planAccountBookId = planAccountBookRepository.save(planAccountBook).getId();
        return planAccountBookId;
    }

    public void planAccountBookDelete(PlanAccountBookDto accountBookDto) {
        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
        Long id = planAccountBook.getId();

        PlanAccountBook planAccountBook2 = planAccountBookRepository.findByIdAndUser_Id(id, accountBookDto.getUser().getId());
        if(!planAccountBook.getUser().getId().equals(planAccountBook2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        planAccountBookRepository.delete(planAccountBook);
    }

    public void planAccountBookDeleteList(Long travelId, User user) {
        List<PlanAccountBook> planAccountBookList = planAccountBookRepository.findAllByTravel_IdAndUser_Id(travelId, user.getId());
        for(PlanAccountBook deleteParam : planAccountBookList) {
            planAccountBookRepository.deleteById(deleteParam.getId());
        }
    }
}