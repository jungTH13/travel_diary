package com.travelProject.travelDiary.entity.plan;

import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.abstractions.CreateEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="tbl_plan_airplane")
public class PlanAirplane {
    @Id
    @Column(name = "pa_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Travel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="t_id")
    private Travel travel;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private User user;

    @Column(name = "pa_title")
    private String title;

    @Column(name = "pa_memo")
    private String memo;

    @Column(name = "pa_depart_location")
    private String departLocation;

    @Column(name="pa_depart_date")
    private Date departDate;

    @Column(name = "pa_arrive_location")
    private String arriveLocation;

    @Column(name="pa_arrive_date")
    private Date arriveDate;

    @Column(name="pa_flight_number")
    private String flightNumber;

    @Column(name="pa_airline")
    private String airline;

    @Column(name="pa_boarding_gate")
    private String boardingGate;

    @Column(name="pa_terminal")
    private String terminal;

    @Column(name="pa_reservation_number")
    private String reservationNumber;

    @Column(nullable = false)
    @ColumnDefault("false")
    protected boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    protected LocalDateTime modifiedDate;
}
