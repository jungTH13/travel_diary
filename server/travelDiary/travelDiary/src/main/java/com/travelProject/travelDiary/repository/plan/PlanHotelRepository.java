package com.travelProject.travelDiary.repository.plan;

import com.travelProject.travelDiary.entity.plan.PlanHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PlanHotelRepository extends JpaRepository<PlanHotel, Long> {

    @Query(value = ""
            + "SELECT\n"
            + "\tt_id AS id\n"
            + "\t, t_title AS title\n"
            + "\t, '' AS thumnail\n"
            + "\t, CAST(t_start_date AS DATE) AS fromDate\n"
            + "\t, CAST(t_end_date AS DATE) AS endDate\n"
            + "FROM tbl_travel\n"
            + "WHERE t_start_date >= NOW()\n"
            + "\tAND id = :#{#userId}"
            ,
            nativeQuery = true)
    List<Map<String, Object>> selectPlanHotelList(@Param(value = "userId")String userId);

    PlanHotel findByIdAndUser_Id(Long t_id,String id);
}