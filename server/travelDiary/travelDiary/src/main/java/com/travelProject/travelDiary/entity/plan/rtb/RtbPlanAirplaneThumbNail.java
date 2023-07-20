package com.travelProject.travelDiary.entity.plan.rtb;

import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.PlanAirPlane;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="rtb_plan_airplane_thumbnail")
public class RtbPlanAirplaneThumbNail {

    @Id
    @Column(name = "rpat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = PlanAirPlane.class, fetch = FetchType.LAZY)
    @JoinColumn(name="pa_id")
    private PlanAirPlane planAirPlane;

    @ManyToOne(targetEntity = Thumbnail.class, fetch = FetchType.LAZY)
    @JoinColumn(name="th_id")
    private Thumbnail thId;
}