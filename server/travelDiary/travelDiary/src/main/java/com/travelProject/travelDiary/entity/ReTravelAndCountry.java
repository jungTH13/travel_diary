package com.travelProject.travelDiary.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name="rtb_travel_country")
public class ReTravelAndCountry {

    @Id
    @Column(name = "rtc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Travel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="t_id")
    private Long travelId;

    @ManyToOne(targetEntity = Country.class, fetch = FetchType.LAZY)
    @JoinColumn(name="c_id")
    private Long CountryId;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean status;
}
