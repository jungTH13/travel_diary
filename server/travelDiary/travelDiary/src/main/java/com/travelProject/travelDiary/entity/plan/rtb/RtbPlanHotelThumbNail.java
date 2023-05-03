package com.travelProject.travelDiary.entity.plan.rtb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.PlanHotel;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="rtb_plan_hotel_thumbnail")
public class RtbPlanHotelThumbNail {

    @Id
    @Column(name = "rpht_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = PlanHotel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="ph_id")
    @JsonIgnore
    private PlanHotel planHotel;

    @ManyToOne(targetEntity = Thumbnail.class, fetch = FetchType.LAZY)
    @JoinColumn(name="th_id")
    @JsonIgnore
    private Thumbnail travel;
}