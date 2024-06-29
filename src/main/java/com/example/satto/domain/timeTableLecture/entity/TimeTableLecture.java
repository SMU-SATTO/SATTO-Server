package com.example.satto.domain.timeTableLecture.entity;

import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import com.example.satto.domain.timeTable.entity.TimeTable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TimeTableLecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeTableLectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_lect_id")
    private CurrentLecture currentLecture;

    public TimeTableLecture(TimeTable timeTable, CurrentLecture lect) {
        this.currentLecture = lect;
        this.timeTable = timeTable;
    }
}
