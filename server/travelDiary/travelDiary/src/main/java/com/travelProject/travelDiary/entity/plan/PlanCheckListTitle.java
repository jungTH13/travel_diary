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
@Table(name="tbl_plan_checkList_title")
public class PlanCheckListTitle {
    @Id
    @Column(name = "pct_id")
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

    @Column(name = "pct_title")
    private String title;

    @Column(name = "pct_memo")
    private String memo;

    @Column(name = "pct_plan_type")
    private String planType;

    @Column(name="pct_plan_type_id")
    private Long planTypeId;

    @Column(name = "pct_require_date")
    private Date requireDate;

    @Column(name = "pct_is_completed")
    private Boolean isCompleted;

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
        map.put("planType", this.planType);
        map.put("planTypeId", this.planTypeId);
        map.put("requireDate", this.requireDate);
        map.put("isCompleted", this.isCompleted);
        return map;
    }
}