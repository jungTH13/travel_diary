package com.travelProject.travelDiary.entity.plan.rtb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.PlanEtc;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="rtb_plan_etc_thumbnail")
public class RtbPlanEtcThumbNail {

    @Id
    @Column(name = "rpet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = PlanEtc.class, fetch = FetchType.LAZY)
    @JoinColumn(name="pe_id")
    @JsonIgnore
    private PlanEtc planEtc;

    @ManyToOne(targetEntity = Thumbnail.class, fetch = FetchType.LAZY)
    @JoinColumn(name="th_id")
    @JsonIgnore
    private Thumbnail thId;
}