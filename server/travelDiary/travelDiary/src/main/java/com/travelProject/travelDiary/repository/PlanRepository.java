package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Plan;
import com.travelProject.travelDiary.interfaces.mapping.PlanMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PlanRepository extends JpaRepository<Plan,Long> {

    @Query(value="" +
            "SELECT" +
            "\n\tp_id as id,"+
            "\n\tdate_format(p_date_time,'%Y-%m-%d') as date,"+
            "\n\tdate_format(p_date_time,'%H:%i:%s') as time,"+
            "\n\tp_title as title,"+
            "\n\tp_memo as memo"+
            "\n\tFROM tbl_plan tp"+
            "\n\tWHERE tp.id = :id"
            ,nativeQuery = true)
    List<Map<String,Object>> findByUser_Id(@Param(value = "id") String id);
}
