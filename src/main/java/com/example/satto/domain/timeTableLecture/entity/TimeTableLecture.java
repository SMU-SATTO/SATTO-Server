package com.example.satto.domain.timeTableLecture.entity;

import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import com.example.satto.domain.timeTable.entity.TimeTable;
import jakarta.persistence.*;

@Entity
public class TimeTableLecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeTableLectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SBJ_DIVCLS")
    private CurrentLecture currentLecture;
}
