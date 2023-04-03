package com.travelProject.travelDiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelDto {

    private Long id;

    private String title;

    private Date startDate;

    private Date endDate;

    private String[] country;
}