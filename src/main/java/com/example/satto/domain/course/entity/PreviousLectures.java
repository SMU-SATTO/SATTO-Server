package com.example.satto.domain.course.entity;

import com.example.satto.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class PreviousLectures extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "previous_lect_id")
    private Long id;

    private String name;
    private String cmp_div;
    private int is_cyber;
    private int credit;
    private String subject_type;
    private String semester_year;
    private String code;
    private String notice;
}
