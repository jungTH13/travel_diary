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
@Table(name="tbl_plan_restaurant")
public class PlanRestaurant {
    @Id
    @Column(name = "pr_id")
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

    @Column(name = "pr_title")
    private String title;

    @Column(name = "pr_memo")
    private String memo;

    @Column(name = "pr_name")
    private String name;

    @Column(name = "pr_date")
    private Date date;

    @Column(name = "pr_address")
    private String address;

    @Column(name = "pr_phone")
    private String phone;

    @Column(name="pr_x")
    private Double x;

    @Column(name="pr_y")
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderDate", this.date);
        map.put("title", this.title);
        map.put("memo", this.memo);
        map.put("name", this.name);
        map.put("date", this.date);
        map.put("address", this.address);
        map.put("phone", this.phone);
        map.put("x", this.x);
        map.put("y", this.y);
        return map;
    }
}