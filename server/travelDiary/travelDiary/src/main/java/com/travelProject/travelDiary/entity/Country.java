package com.travelProject.travelDiary.entity;

import com.travelProject.travelDiary.entity.abstractions.DefaultEntity;
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


@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="tbl_country")
public class Country extends DefaultEntity<Long> {

    @Id
    @Column(name = "c_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_code")
    private String code;

    @Column(name = "c_thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "c_ex_rate_name")
    private String exRateName;

    @Column(name = "c_ex_rate_code")
    private String exRateCode;

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
