package com.travelProject.travelDiary.entity;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // CreatedDate 어노테이션의 자동 생성을 위해 필요
@Table(name="tbl_user")
public class User {

    @Id
    @Column(nullable = false)
    private String id;

    @Column
    private String email;

    @Column(nullable = false)
    private String auth;

    @Column
    private  String publisher;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean status;

    public void patch(User user){
        this.status = user.status;
    }

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedDate;

    @Builder
    public User(String id,String auth){
        this.id = id;
        this.auth = auth;
    }
}
