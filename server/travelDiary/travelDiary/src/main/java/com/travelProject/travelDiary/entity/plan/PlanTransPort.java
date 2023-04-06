package com.travelProject.travelDiary.entity.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
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
@Table(name="tbl_plan_transport")
public class PlanTransPort {
    @Id
    @Column(name = "pt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Travel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="t_id")
    @JsonIgnore
    private Travel travel;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    @JsonIgnore
    private User user;

    @Column(name = "pt_title")
    private String title;

    @Column(name = "pt_memo")
    private String memo;

    @Column(name = "pt_arrive_location")
    private String arriveLocation;

    @Column(name = "pt_arrive_date")
    private Date arriveDate;

    @Column(name = "pt_depart_location")
    private String departLocation;

    @Column(name = "pt_depart_date")
    private Date departDate;

    @Column(name = "pt_reservation_number")
    private String reservationNumber;

    @Column(name = "pt_line")
    private String line;

    @Column(name = "pt_seat_number")
    private String seatNumber;

    @Column(name="pt_x")
    private Double x;

    @Column(name="pt_y")
    private Double y;

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