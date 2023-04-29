package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.plan.*;
import com.travelProject.travelDiary.repository.plan.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanAirPlaneRepository planAirPlaneRepository;

    @Autowired
    private PlanHotelRepository planHotelRepository;

    @Autowired
    private PlanRestaurantRepository planRestaurantRepository;

    @Autowired
    private PlanTransPortRepository planTransPortRepository;

    @Autowired
    private PlanEtcRepository planEtcRepository;

    @Autowired
    private PlanAccountBookRepository planAccountBookRepository;

    @Autowired
    private PlanCheckListTitleRepository planCheckListTitleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getUserPlan(String userId, Long travelId) {
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
            setPlanAccountBookAndCheckList(insertMap, "pa", planAirPlane.getId(), planAccountBookList, planCheckListTitleList);
            planList.add(insertMap);
        }

        for (PlanHotel planHotel : hotelList) {
            Map<String, Object> insertMap = planHotel.toMap();
            setPlanAccountBookAndCheckList(insertMap, "ph", planHotel.getId(), planAccountBookList, planCheckListTitleList);
            planList.add(insertMap);
        }

        for (PlanRestaurant planRestaurant : restaurantList) {
            Map<String, Object> insertMap = planRestaurant.toMap();
            setPlanAccountBookAndCheckList(insertMap, "pr", planRestaurant.getId(), planAccountBookList, planCheckListTitleList);
            planList.add(insertMap);
        }

        for (PlanTransPort planTransPort : transPortList) {
            Map<String, Object> insertMap = planTransPort.toMap();
            setPlanAccountBookAndCheckList(insertMap, "pt", planTransPort.getId(), planAccountBookList, planCheckListTitleList);
            planList.add(insertMap);
        }

        for (PlanEtc planEtc : etcList) {
            Map<String, Object> insertMap = planEtc.toMap();
            setPlanAccountBookAndCheckList(insertMap, "pe", planEtc.getId(), planAccountBookList, planCheckListTitleList);
            planList.add(insertMap);
        }

        planList.sort(Comparator.comparing((Map<String, Object> map) -> (Date) map.get("orderDate")));
        return planList;
    }

    private void setPlanAccountBookAndCheckList(Map<String, Object> insertMapValue, String planType, Long planTypeId
            , List<PlanAccountBook> bookList
            , List<PlanCheckListTitle> checkList) {
        List<PlanAccountBook> accountBookResult = bookList.stream()
                .filter(title -> title.getPlanTypeId() == planTypeId && title.getPlanType().equals(planType))
                .collect(Collectors.toList());

        Optional<PlanCheckListTitle> checkResult = checkList.stream()
                .filter(title -> title.getPlanTypeId() == planTypeId && title.getPlanType().equals(planType))
                .findAny();

        if (accountBookResult.size() > 0) {
            BigDecimal sumAmount = accountBookResult.stream()
                    .map(PlanAccountBook::getAmountOfPayment)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            insertMapValue.put("amountOfPayment", sumAmount);
        }

        if (checkResult.isPresent()) {
            insertMapValue.put("isCompleted", checkResult.map(PlanCheckListTitle::getIsCompleted));
        }
    }
}