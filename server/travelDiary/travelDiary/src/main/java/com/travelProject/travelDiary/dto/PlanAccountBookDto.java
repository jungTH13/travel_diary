package com.travelProject.travelDiary.dto;


import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private Date paymentDate;

    private String paymentType;

    private BigDecimal amountOfPayment;

    private String planType;

    private Long planTypeId;
}