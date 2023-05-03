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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="tbl_plan_account_book")
public class PlanAccountBook {
    @Id
    @Column(name = "pab_id")
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

    @Column(name = "pab_title")
    private String title;

    @Column(name = "pab_memo")
    private String memo;

    @Column(name = "pab_payment_date")
    private Date paymentDate;

    @Column(name = "pab_payment_type")
    private String paymentType;

    @Column(name="pab_amount_of_payment", precision = 20, scale = 5)
    private BigDecimal amountOfPayment;

    @Column(name = "pab_plan_type")
    private String planType;

    @Column(name="pab_plan_type_id")
    private Long planTypeId;

    @Column(name = "pab_category_type")
    private String categoryType;

    @Column(name = "pab_x")
    private Double x;

    @Column(name = "pab_y")
    private Double y;

    @Column(name = "pab_c_id")
    private String cId;


    @Column(nullable = false)
    @ColumnDefault("false")
    protected boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    protected LocalDateTime modifiedDate;
}