package com.travelProject.travelDiary.service.plan;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.*;
import com.travelProject.travelDiary.entity.plan.rtb.*;
import com.travelProject.travelDiary.repository.plan.*;
import com.travelProject.travelDiary.repository.plan.rtb.*;
import com.travelProject.travelDiary.service.ThumbnailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private ThumbnailService thumbnailService;

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

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PlanAccountBookRepository planAccountBookRepository;

    @Autowired
    private PlanCheckListTitleRepository planCheckListTitleRepository;

    public Map<String,Object> getUserPlan(String userId, Long travelId) {
        Map<String, Object> result = new HashMap<>();
        if(userId.equals("") || userId == null) {
            throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);
        }
        List<PlanAirPlane> airPlaneList = planAirPlaneRepository.findAllByTravel_IdAndUser_IdOrderByDepartDateAsc(travelId, userId);
        List<PlanHotel> hotelList = planHotelRepository.findAllByTravel_IdAndUser_IdOrderByCheckinDateAsc(travelId, userId);
        List<PlanRestaurant> restaurantList = planRestaurantRepository.findAllByTravel_IdAndUser_IdOrderByDateAsc(travelId, userId);
        List<PlanTransPort> transPortList = planTransPortRepository.findAllByTravel_IdAndUser_IdOrderByDepartDateAsc(travelId, userId);
        List<PlanEtc> etcList = planEtcRepository.findAllByTravel_IdAndUser_IdOrderByReservationDateAsc(travelId, userId);

        List<PlanAccountBook> planAccountBookList = planAccountBookRepository.findAllByTravel_IdAndUser_Id(travelId, userId);
        List<PlanCheckListTitle> planCheckListTitleList = planCheckListTitleRepository.findAllByTravel_IdAndUser_Id(travelId, userId);

        List<Map<String, Object>> planList = new ArrayList<>();
        for (PlanAirPlane planAirPlane : airPlaneList) {
            Map<String, Object> insertMap = planAirPlane.toMap();
            planAccountBookList = setPlanAccountBookAndCheckList(insertMap, "pa", planAirPlane.getId(), planAccountBookList, planCheckListTitleList);
            List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pa", planAirPlane.getId());
            insertMap.put("thumbNailList", thumbNailList);
            planList.add(insertMap);
        }

        for (PlanHotel planHotel : hotelList) {
            Map<String, Object> insertMap = planHotel.toMap();
            planAccountBookList = setPlanAccountBookAndCheckList(insertMap, "ph", planHotel.getId(), planAccountBookList, planCheckListTitleList);
            List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("ph", planHotel.getId());
            insertMap.put("thumbNailList", thumbNailList);
            planList.add(insertMap);
        }

        for (PlanRestaurant planRestaurant : restaurantList) {
            Map<String, Object> insertMap = planRestaurant.toMap();
            planAccountBookList = setPlanAccountBookAndCheckList(insertMap, "pr", planRestaurant.getId(), planAccountBookList, planCheckListTitleList);
            List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pr", planRestaurant.getId());
            insertMap.put("thumbNailList", thumbNailList);
            planList.add(insertMap);
        }

        for (PlanTransPort planTransPort : transPortList) {
            Map<String, Object> insertMap = planTransPort.toMap();
            planAccountBookList = setPlanAccountBookAndCheckList(insertMap, "pt", planTransPort.getId(), planAccountBookList, planCheckListTitleList);
            List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pt", planTransPort.getId());
            insertMap.put("thumbNailList", thumbNailList);
            planList.add(insertMap);
        }

        for (PlanEtc planEtc : etcList) {
            Map<String, Object> insertMap = planEtc.toMap();
            planAccountBookList = setPlanAccountBookAndCheckList(insertMap, "pe", planEtc.getId(), planAccountBookList, planCheckListTitleList);
            List<Map<String, Object>> thumbNailList = thumbnailService.rtbPlanThumbNailSelect("pe", planEtc.getId());
            insertMap.put("thumbNailList", thumbNailList);
            planList.add(insertMap);
        }

        planList.sort(Comparator.comparing((Map<String, Object> map) -> (Date) map.get("orderDate")));
        planAccountBookList.sort(Comparator.comparing((PlanAccountBook accountBook) -> accountBook.getPaymentDate()));
        result.put("planList", planList);
        result.put("planAccountBookList", planAccountBookList);
        return result;
    }

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

    private List<PlanAccountBook> setPlanAccountBookAndCheckList(Map<String, Object> insertMapValue, String planType, Long planTypeId
            , List<PlanAccountBook> bookList
            , List<PlanCheckListTitle> checkList) {
        List<PlanAccountBook> accountBookResult = bookList.stream()
                .filter(title -> title.getPlanTypeId() == planTypeId && title.getPlanType().equals(planType))
                .collect(Collectors.toList());

        List<PlanCheckListTitle> checkResult = checkList.stream()
                .filter(title -> title.getPlanTypeId() == planTypeId && title.getPlanType().equals(planType))
                .collect(Collectors.toList());

        if (accountBookResult.size() > 0) {
            BigDecimal sumAmount = BigDecimal.ZERO;
            Iterator<PlanAccountBook> accountBookIterator = accountBookResult.iterator();
            while (accountBookIterator.hasNext()) {
                PlanAccountBook accountBookRoot = accountBookIterator.next();
                BigDecimal findSumValue = accountBookRoot.getAmountOfPayment();
                Long id = accountBookRoot.getId();
                if(findSumValue.doubleValue() < 0) {
                    sumAmount = sumAmount.add(findSumValue.negate());
                    bookList.removeIf(bl -> bl.getId() == id);
                }
            }
            insertMapValue.put("sumAmount", sumAmount);
        }

        if (checkResult.size() > 0) {
            List<Map<String, Object>> insertCheckList = new ArrayList<>();
            for(PlanCheckListTitle findCheck : checkResult) {
                Map<String, Object> insertParam = new HashMap<>();
                insertParam.put("title", findCheck.getTitle());
                insertParam.put("isCompleted", findCheck.getIsCompleted());
                insertCheckList.add(insertParam);
            }
            insertMapValue.put("checkList", insertCheckList);
        }

        return bookList;
    }
}