package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TravelService {
    @Autowired
    private TravelRepository travelRepository;

    public List<Map<String, Object>> selectPlanTravelList() {
        return travelRepository.selectPlanTravelList();
    }

    public List<Map<String, Object>> selectEndTravelList() {
        return travelRepository.selectEndTravelList();
    }

    public void travelInsert(Map<String, Object> param) {
        Date date = new Date();
        LocalDateTime time = LocalDateTime.now();

        Travel travel = null;
        travel.setId(travel.getId());
        travel.setTitle("test");
        travel.setUser_id(UUID.randomUUID());
        travel.setStartDate(date);
        travel.setEndDate(date);
        travel.setCreatedDate(time);
        travel.setModifiedDate(time);
        travelRepository.save(travel);
    }
}
