package com.travelProject.travelDiary.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ExchangeRate {

    @Id
    private Date date;

    @Column(nullable = false)
    private String moneyType;

    @Column(nullable = false)
    private Double Rate;
}
