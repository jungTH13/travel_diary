package com.travelProject.travelDiary.entity.plan.rtb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.plan.PlanRestaurant;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="rtb_plan_restaurant_thumbnail")
public class RtbPlanRestaurantThumbNail {

    @Id
    @Column(name = "rprt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = PlanRestaurant.class, fetch = FetchType.LAZY)
    @JoinColumn(name="pr_id")
    @JsonIgnore
    private PlanRestaurant planRestaurant;

    @ManyToOne(targetEntity = Thumbnail.class, fetch = FetchType.LAZY)
    @JoinColumn(name="th_id")
    @JsonIgnore
    private Thumbnail travel;
}