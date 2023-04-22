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
public class PlanAirPlaneDto {

    private Long id;
    private Travel travel;
    private User user;
    private String title;
    private String memo;
    private String departLocation;
    private Date departDate;
    private String arriveLocation;
    private Date arriveDate;
    private String flightNumber;
    private String seatNumber;
    private String airline;
    private String boardingGate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date boardingTime;
    private String terminal;
    private String reservationNumber;
    private Double x;
    private Double y;

    private Double x2;
    private Double y2;
}