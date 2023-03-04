package com.travelProject.travelDiary.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String division;

    @Column(nullable = false)
    private String codeKey;

    @Column(nullable = false)
    private String codeValue;

    @Column
    private String path;

    @Column(nullable = false)
    private boolean status;

}
