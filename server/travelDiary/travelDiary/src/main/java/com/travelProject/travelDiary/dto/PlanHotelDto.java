package com.travelProject.travelDiary.dto;


import com.travelProject.travelDiary.entity.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanHotelDto {
    private Long id;

    //private Long travelId;
    private Travel travel;

    private String user;

}