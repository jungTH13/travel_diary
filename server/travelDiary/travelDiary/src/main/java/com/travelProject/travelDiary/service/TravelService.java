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

    public List<Map<String, Object>> selectPlanTravelList(String userId) {
        return travelRepository.selectPlanTravelList(userId);
    }

    public List<Map<String, Object>> selectEndTravelList(String userId) {
        return travelRepository.selectEndTravelList(userId);
    }

    public void travelSave(Travel travel) {
        LocalDateTime time = LocalDateTime.now();

        Long id = travel.getId();
        if(id == null) {
            travel.setCreatedDate(time);
            travel.setModifiedDate(time);
        } else {
            travel.setModifiedDate(time);
        }

        travelRepository.save(travel);
    }

    public void travelDelete(Travel travel) {
        travelRepository.delete(travel);
    }
}
