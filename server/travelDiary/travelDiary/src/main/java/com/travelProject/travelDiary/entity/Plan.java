package com.travelProject.travelDiary.entity;

import javax.persistence.*;

import com.travelProject.travelDiary.entity.abstractions.DefaultEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name="tbl_plan")
public class Plan extends DefaultEntity<Long> {

    @Id
    @Column(name = "p_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Travel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="t_id")
    private Long travelId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private String UserId;

    @Column(name = "p_type")
    private String type;

    @Column(name = "p_date_time")
    private LocalDateTime dateTime;

    @Column(name = "p_title")
    private String title;

    @Column(name = "p_memo")
    private String memo;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedDate;
}
