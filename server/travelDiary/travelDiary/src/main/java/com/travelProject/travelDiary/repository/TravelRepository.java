package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TravelRepository extends JpaRepository<Travel, Long> {

    @Query(value = "" +
            "SELECT\n" +
            "\tt_id AS id\n" +
            "\t, t_title AS title\n" +
            "\t, '' AS thumnail\n" +
            "\t, t_start_date AS fromDate\n" +
            "\t, t_end_date AS endDate\n" +
            "FROM tbl_travel\n" +
            "WHERE CAST(t_end_date AS DATE) <= CAST(NOW() AS DATE)",
            nativeQuery = true)
    List<Map<String, Object>> selectPlanTravelList();

    @Query(value = "" +
            "SELECT\n" +
            "\tt_id AS id\n" +
            "\t, t_title AS title\n" +
            "\t, '' AS thumnail\n" +
            "\t, t_start_date AS fromDate\n" +
            "\t, t_end_date AS endDate\n" +
            "FROM tbl_travel\n" +
            "WHERE CAST(t_end_date AS DATE) > CAST(NOW() AS DATE)",
            nativeQuery = true)
    List<Map<String, Object>> selectEndTravelList();
}
