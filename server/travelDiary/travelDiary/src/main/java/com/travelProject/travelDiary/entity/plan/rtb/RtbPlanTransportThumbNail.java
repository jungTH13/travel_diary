package com.travelProject.travelDiary.entity.plan.rtb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.PlanTransPort;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="rtb_plan_transport_thumbnail")
public class RtbPlanTransportThumbNail {

    @Id
    @Column(name = "rptt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = PlanTransPort.class, fetch = FetchType.LAZY)
    @JoinColumn(name="pt_id")
    @JsonIgnore
    private PlanTransPort planTransPort;

    @ManyToOne(targetEntity = Thumbnail.class, fetch = FetchType.LAZY)
    @JoinColumn(name="th_id")
    @JsonIgnore
    private Thumbnail thId;
}