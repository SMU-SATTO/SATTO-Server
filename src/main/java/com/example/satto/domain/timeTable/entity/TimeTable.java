package com.example.satto.domain.timeTable.entity;

import com.example.satto.domain.users.entity.Users;
import com.example.satto.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Entity
@Builder
@Getter
public class TimeTable extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long timetableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "timetable_name")
    private String timetableName;
    @Column(name = "is_public")
    private Boolean isPublic;
    @Column(name = "is_represented")
    private Boolean isRepresented;
    @Column(name = "semester_year")
    private String semesterYear;
}
