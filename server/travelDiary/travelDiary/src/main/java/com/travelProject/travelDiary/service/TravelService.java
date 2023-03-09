package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.travelProject.travelDiary.dto.ErrorCode.INVALID_PARAMETER;
import static com.travelProject.travelDiary.dto.ErrorCode.INVALID_USER_PARAMETER;

@Service
public class TravelService {
    @Autowired
    private TravelRepository travelRepository;

    public List<Map<String, Object>> selectPlanTravelList(String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(INVALID_USER_PARAMETER);
        }
        List<Map<String, Object>> resutlList = travelRepository.selectPlanTravelList(userId);
        return resutlList;
    }

    public List<Map<String, Object>> selectEndTravelList(String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(INVALID_USER_PARAMETER);
        }
        List<Map<String, Object>> resutlList = travelRepository.selectEndTravelList(userId);
        return resutlList;
    }

    public void travelInsert(Travel travel) {
        LocalDateTime time = LocalDateTime.now();
        travel.setCreatedDate(time);
        travel.setModifiedDate(time);

        Long id = travel.getId();
        if(id == null) {
            travelRepository.save(travel);
        } else {
            throw new exceptionCode(INVALID_PARAMETER);
        }
    }

    public void travelUpdate(Travel travel) {
        LocalDateTime time = LocalDateTime.now();
        travel.setModifiedDate(time);

        Long id = travel.getId();
        if(id > 0 || id != null) {
            travelRepository.save(travel);
        } else {
            throw new exceptionCode(INVALID_PARAMETER);
        }
    }

    public void travelDelete(Travel travel) {
        travelRepository.delete(travel);
    }
}