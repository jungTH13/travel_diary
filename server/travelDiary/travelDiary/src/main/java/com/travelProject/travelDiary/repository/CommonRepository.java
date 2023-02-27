package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommonRepository extends JpaRepository<Country, Long>  {
    @Query(value = "" +
            "SELECT * " +
            "FROM tbl_country " +
            "",
            nativeQuery = true)
    List<Country> selectListCountry();
}
