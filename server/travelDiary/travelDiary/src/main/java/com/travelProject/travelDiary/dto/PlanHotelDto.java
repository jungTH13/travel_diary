package com.travelProject.travelDiary.dto;


import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanHotelDto {
    private Long id;

    private Travel travel;

    private User user;

    private String title;

    private String memo;

    private Date checkinDate;

    private Date checkoutDate;

    private String name;

    private Date address;

    private String reservationNumber;

    private String phone;

    private Double x;

    private Double y;
}