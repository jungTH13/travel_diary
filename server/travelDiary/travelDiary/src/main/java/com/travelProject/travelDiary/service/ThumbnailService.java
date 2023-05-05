package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.rtb.*;
import com.travelProject.travelDiary.repository.ThumbnailRepository;
import com.travelProject.travelDiary.repository.plan.rtb.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ThumbnailService {

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @Autowired
    private RtbPlanAirplaneThumbNailRepository rtbPlanAirplaneThumbNailRepository;

    @Autowired
    private RtbPlanEtcThumbNailRepository rtbPlanEtcThumbNailRepository;

    @Autowired
    private RtbPlanHotelThumbNailRepository rtbPlanHotelThumbNailRepository;

    @Autowired
    private RtbPlanRestaurantThumbNailRepository rtbPlanRestaurantThumbNailRepository;

    @Autowired
    private RtbPlanTransportThumbNailRepository rtbPlanTransportThumbNailRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Map<String, Object>> rtbPlanThumbNailSelect(String planType, Long planTypeId) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if(planType.equals("pa")) {
            List<RtbPlanAirplaneThumbNail> list = rtbPlanAirplaneThumbNailRepository.findAllByPlanAirPlane_Id(planTypeId);
            for(RtbPlanAirplaneThumbNail param: list) {
                Map<String, Object> insertParam = new HashMap<String, Object>();
                Optional<Thumbnail> thumbnail = thumbnailRepository.findById(param.getThId().getId());
                insertParam.put("id", thumbnail.get().getId());
                insertParam.put("url", thumbnail.get().getUrl());
                resultList.add(insertParam);
            }
        } else if(planType.equals("pe")) {
            List<RtbPlanEtcThumbNail> list = rtbPlanEtcThumbNailRepository.findAllByPlanEtc_Id(planTypeId);
            for(RtbPlanEtcThumbNail param: list) {
                Map<String, Object> insertParam = new HashMap<String, Object>();
                Optional<Thumbnail> thumbnail = thumbnailRepository.findById(param.getThId().getId());
                insertParam.put("id", thumbnail.get().getId());
                insertParam.put("url", thumbnail.get().getUrl());
                resultList.add(insertParam);
            }
        } else if(planType.equals("ph")) {
            List<RtbPlanHotelThumbNail> list = rtbPlanHotelThumbNailRepository.findAllByPlanHotel_Id(planTypeId);
            for(RtbPlanHotelThumbNail param: list) {
                Map<String, Object> insertParam = new HashMap<String, Object>();
                Optional<Thumbnail> thumbnail = thumbnailRepository.findById(param.getThId().getId());
                insertParam.put("id", thumbnail.get().getId());
                insertParam.put("url", thumbnail.get().getUrl());
                resultList.add(insertParam);
            }
        } else if(planType.equals("pr")) {
            List<RtbPlanRestaurantThumbNail> list = rtbPlanRestaurantThumbNailRepository.findAllByPlanRestaurant_Id(planTypeId);
            for(RtbPlanRestaurantThumbNail param: list) {
                Map<String, Object> insertParam = new HashMap<String, Object>();
                Optional<Thumbnail> thumbnail = thumbnailRepository.findById(param.getThId().getId());
                insertParam.put("id", thumbnail.get().getId());
                insertParam.put("url", thumbnail.get().getUrl());
                resultList.add(insertParam);
            }
        } else if(planType.equals("pt")) {
            List<RtbPlanTransportThumbNail> list = rtbPlanTransportThumbNailRepository.findAllByPlanTransPort_Id(planTypeId);
            for(RtbPlanTransportThumbNail param: list) {
                Map<String, Object> insertParam = new HashMap<String, Object>();
                Optional<Thumbnail> thumbnail = thumbnailRepository.findById(param.getThId().getId());
                insertParam.put("id", thumbnail.get().getId());
                insertParam.put("url", thumbnail.get().getUrl());
                resultList.add(insertParam);
            }
        }
        return resultList;
    }

    public Long thumbnailInsert(Thumbnail thumbnail) {
        LocalDateTime time = LocalDateTime.now();
        thumbnail.setCreatedDate(time);

        Long id = thumbnail.getId();
        if(id == null) {
            Long thumbnailId = thumbnailRepository.save(thumbnail).getId();
            return thumbnailId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public void thumbnailDelete(Thumbnail thumbnail) {
        Optional<Thumbnail> deleteParam = thumbnailRepository.findById(thumbnail.getId());
        if(!deleteParam.isEmpty()) {
            Long deleteId = deleteParam.get().getId();
            if(deleteId > 0) {
                thumbnailRepository.delete(thumbnail);
            }
        }
    }
}