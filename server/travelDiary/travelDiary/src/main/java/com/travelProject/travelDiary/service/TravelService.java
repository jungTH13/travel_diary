package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.Travel;
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

    public List<Map<String, Object>> selectPlanTravelList() {
        return travelRepository.selectPlanTravelList();
    }

    public List<Map<String, Object>> selectEndTravelList() {
        return travelRepository.selectEndTravelList();
    }

    public void travelInsert(Travel travel) {
        LocalDateTime time = LocalDateTime.now();
        travel.setCreatedDate(time);
        travel.setModifiedDate(time);
        travelRepository.save(travel);
    }
}
