package com.travelProject.travelDiary.entity.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Table(name="tbl_plan_airplane")
public class PlanAirPlane {
    @Id
    @Column(name = "pa_id")
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

    @Column(name="pa_seat_number")
    private String seatNumber;

    @Column(name="pa_airline")
    private String airline;

    @Column(name="pa_boarding_gate")
    private String boardingGate;

    @Column(name="pa_boarding_time")
    private Date boardingTime;
    @Column(name="pa_terminal")
    private String terminal;

    @Column(name="pa_reservation_number")
    private String reservationNumber;

    @Column(name = "pa_c_id")
    private String cId;

    @Column(name="pa_x")
    private Double x;

    @Column(name="pa_y")
    private Double y;

    @Column(name="pa_x2")
    private Double x2;

    @Column(name="pa_y2")
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
        map.put("type", "pa");
        map.put("id", this.id);
        map.put("orderDate", this.departDate);
        map.put("title", this.title);
        map.put("memo", this.memo);
        map.put("departLocation", this.departLocation);
        map.put("departDate", this.departDate);
        map.put("arriveLocation", this.arriveLocation);
        map.put("arriveDate", this.arriveDate);
        map.put("flightNumber", this.flightNumber);
        map.put("seatNumber", this.seatNumber);
        map.put("airline", this.airline);
        map.put("boardingGate", this.boardingGate);
        map.put("boardingTime", this.boardingTime);
        map.put("terminal", this.terminal);
        map.put("reservationNumber", this.reservationNumber);
        map.put("cId", this.cId);
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("x2", this.x2);
        map.put("y2", this.y2);
        return map;
    }
}
