package com.travelProject.travelDiary.dto;


import com.travelProject.travelDiary.entity.plan.PlanCheckListTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCheckListDetailDto {
    private Long id;

    private PlanCheckListTitle planCheckListTitle;

    private String detail;

    private Boolean checked;
}