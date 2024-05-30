package com.example.satto.domain.timeTableLecture.repository;

import com.example.satto.domain.timeTableLecture.entity.TimeTableLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableLectureRepository extends JpaRepository<TimeTableLecture, Long> {
}
