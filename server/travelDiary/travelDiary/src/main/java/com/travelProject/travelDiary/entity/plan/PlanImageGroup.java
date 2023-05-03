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
@Table(name="tbl_plan_image_group")
public class PlanImageGroup {
    @Id
    @Column(name = "pig_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    @JsonIgnore
    private User user;

    @Column(name = "pig_title")
    private String title;

    @Column(name = "pig_memo")
    private String memo;

    @Column(name="pig_x")
    private Double x;

    @Column(name="pig_y")
    private Double y;

    @Column(name = "pig_c_id")
    private String cId;

    @Column(name = "pig_date")
    private Date date;

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
        map.put("id", this.id);
        map.put("title", this.title);
        map.put("memo", this.memo);
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("cId", this.cId);
        map.put("date", this.date);
        return map;
    }
}