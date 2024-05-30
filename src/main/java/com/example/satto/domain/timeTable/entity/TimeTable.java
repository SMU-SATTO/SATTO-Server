package com.example.satto.domain.timeTable.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class TimeTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long timetableId;

    @Column(name = "is_public")
    private Boolean isPublic;
    @Column(name = "is_represented")
    private Boolean isRepresented;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "semester_year")
    private String semesterYear;

}
