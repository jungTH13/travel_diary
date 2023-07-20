package com.travelProject.travelDiary.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanAccountBookDto {
    private Long id;

    private Travel travel;

    private User user;

    private String title;

    private String memo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date paymentDate;

    private String paymentType;

    private BigDecimal amountOfPayment;

    private String planType;

    private Long planTypeId;

    private String categoryType;

    private Double x;

    private Double y;

    private String cId;
}