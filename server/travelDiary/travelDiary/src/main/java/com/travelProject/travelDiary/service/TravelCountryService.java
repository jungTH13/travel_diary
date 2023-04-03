package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.Country;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.TravelCountry;
import com.travelProject.travelDiary.repository.TravelCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TravelCountryService {
    @Autowired
    private TravelCountryRepository travelCountryRepository;

    public void travelCountryInsert(Long travelId, String countryStr) {
        Long countryId = Long.valueOf(countryStr.substring(0, countryStr.indexOf("_")));
        TravelCountry travelCountry = new TravelCountry();

        Travel travel = new Travel();
        travel.setId(travelId);
        travelCountry.setTravel(travel);

        Country country = new Country();
        country.setId(countryId);
        travelCountry.setCountry(country);

        travelCountryRepository.save(travelCountry);
    }

    public void travelCountryDelete(Long travelId) {
        List<TravelCountry> travelCountryList = travelCountryRepository.findAllByTravel_Id(travelId);
        for (TravelCountry travelCountry: travelCountryList) {
            Long travelCountryId = travelCountry.getId();
            travelCountryRepository.deleteById(travelCountryId);
        }
    }
}