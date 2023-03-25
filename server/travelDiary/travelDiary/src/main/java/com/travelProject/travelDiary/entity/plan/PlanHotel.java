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
@Table(name="tbl_plan_hotel")
public class PlanHotel {
    @Id
    @Column(name = "ph_id")
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

    @Column(name = "ph_title")
    private String title;

    @Column(name = "ph_memo")
    private String memo;

    @Column(name = "ph_checkin_date")
    private Date checkinDate;

    @Column(name = "ph_checkout_date")
    private Date checkoutDate;

    @Column(name = "ph_name")
    private String name;

    @Column(name="ph_address")
    private Date address;

    @Column(name = "ph_reservation_number")
    private String reservationNumber;

    @Column(name="ph_phone")
    private String phone;

    @Column(name="ph_x")
    private Double x;

    @Column(name="ph_y")
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