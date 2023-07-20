package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.TravelCountry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelCountryRepository extends JpaRepository<TravelCountry, Long> {

    List<TravelCountry> findAllByTravel_Id(Long travelId);
}