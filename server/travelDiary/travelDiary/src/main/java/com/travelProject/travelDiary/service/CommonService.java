package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.Country;

import com.travelProject.travelDiary.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommonService {
    @Autowired
    private CommonRepository commonRepository;

    public List<Map<String, Object>> selectListCountry() {
        return commonRepository.selectListCountry();
    }
}
