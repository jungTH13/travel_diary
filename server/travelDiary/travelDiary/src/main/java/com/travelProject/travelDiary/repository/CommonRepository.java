package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CommonRepository extends JpaRepository<Country, Long>  {
    @Query(value = "" +
            "SELECT\n" +
            " \tCONCAT(c_id, '_', c_code) AS code\n" +
            " \t, c_name AS name\n" +
            " \t, IFNULL(c_thumbnail_url, '') AS thumbnail\n" +
            "FROM tbl_country",
            nativeQuery = true)
    List<Map<String, Object>> selectListCountry();

    @Query(value = "" +
            "SELECT\n" +
            " \tCONCAT(c_id, '_', c_code) AS code\n" +
            " \t, c_name AS name\n" +
            " \t, IFNULL(c_thumbnail_url, '') AS thumbnail\n" +
            "FROM tbl_country\n" +
            "WHERE CONCAT(c_id, '_', c_code) = :#{#code}",
            nativeQuery = true)
    List<Map<String, Object>> selectCountry(@Param(value = "code")String code);

    @Query(value = "" +
            "SELECT\n" +
            " \tCONCAT(c_id, '_', c_code) AS code\n" +
            " \t, c_name AS name\n" +
            " \t, IFNULL(c_thumbnail_url, '') AS thumbnail\n" +
            "FROM tbl_country\n" +
            "WHERE c_name LIKE :#{#name}",
            nativeQuery = true)
    List<Map<String, Object>> selectCountryLike(@Param(value = "name")String name);
}