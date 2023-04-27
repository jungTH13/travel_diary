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
import java.util.HashMap;
import java.util.Map;

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

    @Column(name="pt_boarding_gate")
    private String boardingGate;

    @Column(name = "pt_reservation_number")
    private String reservationNumber;

    @Column(name = "pt_line")
    private String line;

    @Column(name = "pt_seat_number")
    private String seatNumber;

    @Column(name = "pt_c_id")
    private String cId;

    @Column(name = "pt_c_id2")
    private String cId2;

    @Column(name="pt_x")
    private Double x;

    @Column(name="pt_y")
    private Double y;

    @Column(name="pt_x2")
    private Double x2;

    @Column(name="pt_y2")
    private Double y2;

    @Column(nullable = false)
    @ColumnDefault("false")
    protected boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    protected LocalDateTime modifiedDate;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "pt");
        map.put("id", this.id);
        map.put("orderDate", this.departDate);
        map.put("title", this.title);
        map.put("memo", this.memo);
        map.put("arriveLocation", this.arriveLocation);
        map.put("arriveDate", this.arriveDate);
        map.put("departLocation", this.departLocation);
        map.put("departDate", this.departDate);
        map.put("boardingGate", this.boardingGate);
        map.put("reservationNumber", this.reservationNumber);
        map.put("line", this.line);
        map.put("seatNumber", this.seatNumber);
        map.put("cId", this.cId);
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("x2", this.x2);
        map.put("y2", this.y2);
        return map;
    }
}