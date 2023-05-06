package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.*;
import com.travelProject.travelDiary.entity.plan.rtb.*;
import com.travelProject.travelDiary.repository.plan.*;
import com.travelProject.travelDiary.repository.plan.rtb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PlanCommonService {
    @Autowired
    private PlanAirPlaneRepository planAirPlaneRepository;

    @Autowired
    private PlanEtcRepository planEtcRepository;

    @Autowired
    private PlanHotelRepository planHotelRepository;

    @Autowired
    private PlanRestaurantRepository planRestaurantRepository;

    @Autowired
    private PlanTransPortRepository planTransPortRepository;

    @Autowired
    private PlanImageGroupRepository planImageGroupRepository;

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
    private RtbPlanImageGroupThumbNailRepository rtbPlanImageGroupThumbNailRepository;

    public Long planMemoUpdate(Long travelId, String userId, Map<String, Object> param) {
        LocalDateTime time = LocalDateTime.now();
        String memo = (String) param.getOrDefault("memo","");
        String planType = (String) param.getOrDefault("planType","");
        Long planId = Long.parseLong(param.getOrDefault("planId","0").toString());

        LocalDateTime localDateTime = LocalDateTime.now();

        Long resultPlanId = 0L;

        if(travelId < 0 || travelId == null) {
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        if((planId <= 0 || planId == null)
                || !(planType.equals("pa") || planType.equals("pe") || planType.equals("ph")
                || planType.equals("pr") || planType.equals("pt") || planType.equals("pig"))
        ) {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }

        if(planType.equals("pa")) {
            PlanAirPlane planAirPlane = planAirPlaneRepository.findByIdAndUser_Id(planId, userId);

            if(planAirPlane == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planAirPlane.setMemo(memo);
            planAirPlane.setModifiedDate(time);
            resultPlanId = planAirPlaneRepository.save(planAirPlane).getId();
        } else if(planType.equals("pe")) {
            PlanEtc planEtc = planEtcRepository.findByIdAndUser_Id(planId, userId);

            if(planEtc == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planEtc.setMemo(memo);
            planEtc.setModifiedDate(time);
            resultPlanId = planEtcRepository.save(planEtc).getId();
        } else if(planType.equals("ph")) {
            PlanHotel planHotel = planHotelRepository.findByIdAndUser_Id(planId, userId);

            if(planHotel == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planHotel.setMemo(memo);
            planHotel.setModifiedDate(time);
            resultPlanId = planHotelRepository.save(planHotel).getId();
        } else if(planType.equals("pr")) {
            PlanRestaurant planRestaurant = planRestaurantRepository.findByIdAndUser_Id(planId, userId);

            if(planRestaurant == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planRestaurant.setMemo(memo);
            planRestaurant.setModifiedDate(time);
            resultPlanId = planRestaurantRepository.save(planRestaurant).getId();
        } else if(planType.equals("pt")) {
            PlanTransPort planTransPort = planTransPortRepository.findByIdAndUser_Id(planId, userId);

            if(planTransPort == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planTransPort.setMemo(memo);
            planTransPort.setModifiedDate(time);
            resultPlanId = planTransPortRepository.save(planTransPort).getId();
        } else if(planType.equals("pig")) {
            PlanImageGroup planImageGroup = planImageGroupRepository.findByIdAndUser_Id(planId, userId);

            if(planImageGroup == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
            planImageGroup.setMemo(memo);
            planImageGroup.setModifiedDate(time);
            resultPlanId = planImageGroupRepository.save(planImageGroup).getId();
        }
        return resultPlanId;
    }

    public void planIdSelect(String userId, String planType, Long planTypeId) {
        if(planType.equals("pa")) {
            PlanAirPlane planAirPlane = planAirPlaneRepository.findByIdAndUser_Id(planTypeId, userId);

            if(planAirPlane == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
        } else if(planType.equals("pe")) {
            PlanEtc planEtc = planEtcRepository.findByIdAndUser_Id(planTypeId, userId);

            if(planEtc == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
        } else if(planType.equals("ph")) {
            PlanHotel planHotel = planHotelRepository.findByIdAndUser_Id(planTypeId, userId);

            if(planHotel == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
        } else if(planType.equals("pr")) {
            PlanRestaurant planRestaurant = planRestaurantRepository.findByIdAndUser_Id(planTypeId, userId);

            if(planRestaurant == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
        } else if(planType.equals("pt")) {
            PlanTransPort planTransPort = planTransPortRepository.findByIdAndUser_Id(planTypeId, userId);

            if(planTransPort == null) {
                throw new exceptionCode(ErrorCode.INVALID_ID_PARAMETER);
            }
        }
    }
    public void rtbThumbNailInsert(String planType, Long planTypeId, Long thumbNailId) {
        if((planTypeId <= 0 || planTypeId == null)
                || !(planType.equals("pa") || planType.equals("pe") || planType.equals("ph")
                || planType.equals("pr") || planType.equals("pt") || planType.equals("pig"))
        ) {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
        Thumbnail thumbnail = new Thumbnail();

        if(planType.equals("pa")) {
            RtbPlanAirplaneThumbNail rtbPlanAirplaneThumbNail = new RtbPlanAirplaneThumbNail();
            PlanAirPlane planAirPlane = new PlanAirPlane();

            thumbnail.setId(thumbNailId);
            rtbPlanAirplaneThumbNail.setThId(thumbnail);

            planAirPlane.setId(planTypeId);
            rtbPlanAirplaneThumbNail.setPlanAirPlane(planAirPlane);

            rtbPlanAirplaneThumbNailRepository.save(rtbPlanAirplaneThumbNail);
        } else if(planType.equals("pe")) {
            RtbPlanEtcThumbNail rtbPlanEtcThumbNail = new RtbPlanEtcThumbNail();
            PlanEtc planEtc = new PlanEtc();

            thumbnail.setId(thumbNailId);
            rtbPlanEtcThumbNail.setThId(thumbnail);

            planEtc.setId(planTypeId);
            rtbPlanEtcThumbNail.setPlanEtc(planEtc);

            rtbPlanEtcThumbNailRepository.save(rtbPlanEtcThumbNail);
        } else if(planType.equals("ph")) {
            RtbPlanHotelThumbNail rtbPlanHotelThumbNail = new RtbPlanHotelThumbNail();
            PlanHotel planHotel = new PlanHotel();

            thumbnail.setId(thumbNailId);
            rtbPlanHotelThumbNail.setThId(thumbnail);

            planHotel.setId(planTypeId);
            rtbPlanHotelThumbNail.setPlanHotel(planHotel);

            rtbPlanHotelThumbNailRepository.save(rtbPlanHotelThumbNail);
        } else if(planType.equals("pr")) {
            RtbPlanRestaurantThumbNail rtbPlanRestaurantThumbNail = new RtbPlanRestaurantThumbNail();
            PlanRestaurant planRestaurant = new PlanRestaurant();

            thumbnail.setId(thumbNailId);
            rtbPlanRestaurantThumbNail.setThId(thumbnail);

            planRestaurant.setId(planTypeId);
            rtbPlanRestaurantThumbNail.setPlanRestaurant(planRestaurant);

            rtbPlanRestaurantThumbNailRepository.save(rtbPlanRestaurantThumbNail);
        } else if(planType.equals("pt")) {
            RtbPlanTransportThumbNail rtbPlanTransportThumbNail = new RtbPlanTransportThumbNail();
            PlanTransPort planTransPort = new PlanTransPort();

            thumbnail.setId(thumbNailId);
            rtbPlanTransportThumbNail.setThId(thumbnail);

            planTransPort.setId(planTypeId);
            rtbPlanTransportThumbNail.setPlanTransPort(planTransPort);

            rtbPlanTransportThumbNailRepository.save(rtbPlanTransportThumbNail);
        } else if(planType.equals("pig")) {
            RtbPlanImageGroupThumbNail rtbPlanImageGroupThumbNail = new RtbPlanImageGroupThumbNail();
            PlanImageGroup planImageGroup = new PlanImageGroup();

            thumbnail.setId(thumbNailId);
            rtbPlanImageGroupThumbNail.setThId(thumbnail);

            planImageGroup.setId(planTypeId);
            rtbPlanImageGroupThumbNail.setPlanImageGroup(planImageGroup);

            rtbPlanImageGroupThumbNailRepository.save(rtbPlanImageGroupThumbNail);
        }
    }

    public void rtbThumbNailDelete(String planType, Long planTypeId, Long thumbNailId, String userId) {
        if((planTypeId <= 0 || planTypeId == null)
                || !(planType.equals("pa") || planType.equals("pe") || planType.equals("ph")
                || planType.equals("pr") || planType.equals("pt") || planType.equals("pig"))
        ) {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
        Thumbnail thumbnail = new Thumbnail();

        if(planType.equals("pa")) {
            List<RtbPlanAirplaneThumbNail> list = rtbPlanAirplaneThumbNailRepository.findAllByThId_IdAndPlanAirPlane_User_Id(thumbNailId, userId);
            for(RtbPlanAirplaneThumbNail deleteParam :list) {
                rtbPlanAirplaneThumbNailRepository.deleteById(deleteParam.getId());
            }
        } else if(planType.equals("pe")) {
            List<RtbPlanEtcThumbNail> list = rtbPlanEtcThumbNailRepository.findAllByThId_Id(thumbNailId);
            for(RtbPlanEtcThumbNail deleteParam :list) {
                rtbPlanEtcThumbNailRepository.deleteById(deleteParam.getId());
            }
        } else if(planType.equals("ph")) {
            List<RtbPlanHotelThumbNail> list = rtbPlanHotelThumbNailRepository.findAllByThId_Id(thumbNailId);
            for(RtbPlanHotelThumbNail deleteParam :list) {
                rtbPlanHotelThumbNailRepository.deleteById(deleteParam.getId());
            }
        } else if(planType.equals("pr")) {
            List<RtbPlanRestaurantThumbNail> list = rtbPlanRestaurantThumbNailRepository.findAllByThId_Id(thumbNailId);
            for(RtbPlanRestaurantThumbNail deleteParam :list) {
                rtbPlanRestaurantThumbNailRepository.deleteById(deleteParam.getId());
            }
        } else if(planType.equals("pt")) {
            List<RtbPlanTransportThumbNail> list = rtbPlanTransportThumbNailRepository.findAllByThId_Id(thumbNailId);
            for(RtbPlanTransportThumbNail deleteParam :list) {
                rtbPlanTransportThumbNailRepository.deleteById(deleteParam.getId());
            }
        } else if(planType.equals("pig")) {
            List<RtbPlanImageGroupThumbNail> list = rtbPlanImageGroupThumbNailRepository.findAllByThId_Id(thumbNailId);
            for(RtbPlanImageGroupThumbNail deleteParam :list) {
                rtbPlanImageGroupThumbNailRepository.deleteById(deleteParam.getId());
            }
        }
    }
}