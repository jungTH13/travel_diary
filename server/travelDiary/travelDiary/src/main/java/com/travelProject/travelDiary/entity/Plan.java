package com.travelProject.travelDiary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name="tbl_plan")
public class Plan {

    @Id
    @Column(name = "p_id")
    private Long id;

    @ManyToOne(targetEntity = Travel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="t_id")
    private Long travelId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Long UserId;

    @Column(name = "p_type")
    private String type;

    @Column(name = "p_date_time")
    private LocalDateTime dateTime;

    @Column(name = "p_title")
    private String title;

    @Column(name = "p_memo")
    private String memo;

}
