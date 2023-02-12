package com.travelProject.travelDiary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
