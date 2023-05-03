package com.travelProject.travelDiary.entity.plan.rtb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.PlanImageGroup;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="rtb_plan_image_group_thumbnail")
public class RtbPlanImageGroupThumbNail {

    @Id
    @Column(name = "rpigt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = PlanImageGroup.class, fetch = FetchType.LAZY)
    @JoinColumn(name="pt_id")
    @JsonIgnore
    private PlanImageGroup planImageGroup;

    @ManyToOne(targetEntity = Thumbnail.class, fetch = FetchType.LAZY)
    @JoinColumn(name="th_id")
    @JsonIgnore
    private Thumbnail travel;
}