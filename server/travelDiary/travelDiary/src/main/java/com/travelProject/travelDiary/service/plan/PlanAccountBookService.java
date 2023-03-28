package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.PlanAccountBookDto;
import com.travelProject.travelDiary.entity.plan.PlanAccountBook;
import com.travelProject.travelDiary.repository.plan.PlanAccountBookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.travelProject.travelDiary.dto.ErrorCode.*;

@Service
public class PlanAccountBookService {
    @Autowired
    private PlanAccountBookRepository planAccountBookRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlanAccountBook selectPlanAccountBookOne(PlanAccountBookDto accountBookDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(INVALID_USER_PARAMETER);
        }

        Long planAccountBookId = accountBookDto.getId();
        PlanAccountBook planAccountBook = planAccountBookRepository.findByIdAndUser_Id(planAccountBookId, userId);
        return planAccountBook;
    }

    public List<PlanAccountBook> selectPlanAccountBookList(PlanAccountBookDto accountBookDto, String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(INVALID_USER_PARAMETER);
        }

        Long travelId = accountBookDto.getTravel().getId();
        List<PlanAccountBook> planAccountBookList = planAccountBookRepository.findAllByTravel_IdAndUser_Id(travelId, userId);
        return planAccountBookList;
    }

    public void planAccountBookInsert(PlanAccountBookDto accountBookDto) {
        LocalDateTime time = LocalDateTime.now();
        accountBookDto.setCreatedDate(time);
        accountBookDto.setModifiedDate(time);

        Long id = accountBookDto.getId();
        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
        if(id == null) {
            planAccountBookRepository.save(planAccountBook);
        } else {
            throw new exceptionCode(INVALID_PARAMETER);
        }
    }

    public void planAccountBookUpdate(PlanAccountBookDto accountBookDto) {
        LocalDateTime time = LocalDateTime.now();
        accountBookDto.setModifiedDate(time);

        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
        Long id = planAccountBook.getId();

        PlanAccountBook planAccountBook2 = planAccountBookRepository.findByIdAndUser_Id(id, planAccountBook.getUser().getId());
        if(!planAccountBook.getUser().getId().equals(planAccountBook2.getUser().getId())){
            throw new exceptionCode(DIFFERENT_USER_PARAMETER);
        }

        if(id > 0 || id != null) {
            planAccountBookRepository.save(planAccountBook);
        } else {
            throw new exceptionCode(INVALID_PARAMETER);
        }
    }

    public void planAccountBookDelete(PlanAccountBookDto accountBookDto) {
        PlanAccountBook planAccountBook = modelMapper.map(accountBookDto, PlanAccountBook.class);
        Long id = planAccountBook.getId();

        PlanAccountBook planAccountBook2 = planAccountBookRepository.findByIdAndUser_Id(id, accountBookDto.getUser().getId());
        if(!planAccountBook.getUser().getId().equals(planAccountBook2.getUser().getId())){
            throw new exceptionCode(DIFFERENT_USER_PARAMETER);
        }

        planAccountBookRepository.delete(planAccountBook);
    }
}