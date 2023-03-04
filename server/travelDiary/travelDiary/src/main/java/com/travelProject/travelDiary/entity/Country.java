package com.travelProject.travelDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name="tbl_country")
public class Country {

    @Id
    @Column(name = "c_id")
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedDate;
}
