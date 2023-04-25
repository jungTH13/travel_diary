package com.travelProject.travelDiary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanTransPortDto {
    private Long id;

    private Travel travel;

    private User user;

    private String title;

    private String memo;

    private String arriveLocation;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date arriveDate;

    private String departLocation;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date departDate;

    private String boardingGate;

    private String reservationNumber;

    private String line;

    private String seatNumber;

    private Double x;

    private Double y;

    private Double x2;

    private Double y2;
}