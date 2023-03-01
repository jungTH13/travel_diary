package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CommonRepository extends JpaRepository<Country, Long>  {
    @Query(value = "" +
            "SELECT c_code AS code, c_name AS name, IFNULL(c_thumbnail_url, '') AS thumnai FROM tbl_country",
            nativeQuery = true)
    List<Map<String, Object>> selectListCountry();
}
