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
    private RtbPlanImageGroupThumbNailRepository rtbPlanImageGroupThumbNailRepository;

    @Autowired
    private RtbPlanTransportThumbNailRepository rtbPlanTransportThumbNailRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Map<String, Object>> rtbPlanThumbNailSelect(String planType, Long planTypeId) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<Long> IdList = null;
        if(planType.equals("pa")) {
            IdList = new ArrayList<>();
            List<RtbPlanAirplaneThumbNail> list = rtbPlanAirplaneThumbNailRepository.findAllByPlanAirPlane_Id(planTypeId);
            for(RtbPlanAirplaneThumbNail param: list) {
                IdList.add(param.getThId().getId());
            }

            thumbnailListInsert(IdList, resultList);
        } else if(planType.equals("pe")) {
            IdList = new ArrayList<>();
            List<RtbPlanEtcThumbNail> list = rtbPlanEtcThumbNailRepository.findAllByPlanEtc_Id(planTypeId);
            for(RtbPlanEtcThumbNail param: list) {
                IdList.add(param.getThId().getId());
            }

            thumbnailListInsert(IdList, resultList);
        } else if(planType.equals("ph")) {
            IdList = new ArrayList<>();
            List<RtbPlanHotelThumbNail> list = rtbPlanHotelThumbNailRepository.findAllByPlanHotel_Id(planTypeId);
            for(RtbPlanHotelThumbNail param: list) {
                IdList.add(param.getThId().getId());
            }

            thumbnailListInsert(IdList, resultList);
        } else if(planType.equals("pr")) {
            IdList = new ArrayList<>();
            List<RtbPlanRestaurantThumbNail> list = rtbPlanRestaurantThumbNailRepository.findAllByPlanRestaurant_Id(planTypeId);
            for(RtbPlanRestaurantThumbNail param: list) {
                IdList.add(param.getThId().getId());
            }

            thumbnailListInsert(IdList, resultList);
        } else if(planType.equals("pt")) {
            IdList = new ArrayList<>();
            List<RtbPlanTransportThumbNail> list = rtbPlanTransportThumbNailRepository.findAllByPlanTransPort_Id(planTypeId);
            for(RtbPlanTransportThumbNail param: list) {
                IdList.add(param.getThId().getId());
            }

            thumbnailListInsert(IdList, resultList);
        } else if(planType.equals("pig")) {
            IdList = new ArrayList<>();
            List<RtbPlanImageGroupThumbNail> list = rtbPlanImageGroupThumbNailRepository.findAllByPlanImageGroup_Id(planTypeId);
            for(RtbPlanImageGroupThumbNail param: list) {
                IdList.add(param.getThId().getId());
            }

            thumbnailListInsert(IdList, resultList);
        }
        return resultList;
    }

    public void thumbnailListInsert(List<Long> idList, List<Map<String, Object>> reusltList){
        List<Thumbnail> thumbnailList = thumbnailRepository.findByIdIn(idList);
        for(Thumbnail thumbnail: thumbnailList) {
            Map<String, Object> insertParam = new HashMap<String, Object>();
            insertParam.put("id", thumbnail.getId());
            insertParam.put("url", thumbnail.getUrl());
            insertParam.put("originalUrl", thumbnail.getOriginalUrl());
            reusltList.add(insertParam);
        }
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

    public void thumbnailDelete(Thumbnail thumbnail, String userId) {
        Optional<Thumbnail> deleteParam = thumbnailRepository.findById(thumbnail.getId());
        if(!deleteParam.isEmpty()) {
            Long deleteId = deleteParam.get().getId();
            if(deleteId > 0) {
                thumbnailRepository.delete(thumbnail);
            }
        }
    }
}