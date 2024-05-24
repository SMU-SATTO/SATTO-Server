package com.example.satto.domain.course.entity;

import com.example.satto.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class PreviousLecture extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "previous_lect_id")
    private Long id;

    private String name;
    private String cmpDiv;
    private int isCyber;
    private int credit;
    private String subjectType;
    private String semesterYear;
    private String code;
    private String professor;
    private String notice;

    @OneToMany(mappedBy = "previousLecture")
    private List<Course> courseList = new ArrayList<>();
}
