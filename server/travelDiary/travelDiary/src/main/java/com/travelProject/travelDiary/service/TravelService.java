package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.TravelCountry;
import com.travelProject.travelDiary.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TravelService {
    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TravelCountryService travelCountryService;

    public List<Map<String, Object>> selectPlanTravelList(String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }

        List<Map<String, Object>> resutlList = travelRepository.selectPlanTravelList(userId);
        return resutlList;
    }

    public List<Map<String, Object>> selectEndTravelList(String userId) {
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }
        List<Map<String, Object>> resutlList = travelRepository.selectEndTravelList(userId);
        return resutlList;
    }

    public Long travelInsert(Travel travel) {
        LocalDateTime time = LocalDateTime.now();
        travel.setCreatedDate(time);
        travel.setModifiedDate(time);

        Long id = travel.getId();
        if(id == null) {
            Long travelId = travelRepository.save(travel).getId();
            return travelId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public Long travelUpdate(Travel travel, String[] countryArr) {
        LocalDateTime time = LocalDateTime.now();
        travel.setModifiedDate(time);

        Long id = travel.getId();
        Travel travel2 = travelRepository.findByIdAndUser_Id(id, travel.getUser().getId());
        if(!travel.getUser().getId().equals(travel2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        if(id > 0 || id != null) {
            Long travelId = travelRepository.save(travel).getId();
            travelCountryService.travelCountryDelete(travelId);
            for (String country: countryArr) {
                travelCountryService.travelCountryInsert(travelId, country);
            }
            return travelId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public void travelDelete(Travel travel) {
        Long id = travel.getId();
        Travel travel2 = travelRepository.findByIdAndUser_Id(id, travel.getUser().getId());
        if(!travel.getUser().getId().equals(travel2.getUser().getId())){
            throw new exceptionCode(ErrorCode.DIFFERENT_USER_PARAMETER);
        }

        travelRepository.delete(travel);
    }
}