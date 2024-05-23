package com.example.satto.domain.course.repository;

import com.example.satto.domain.course.entity.Course;
import com.example.satto.domain.course.entity.PreviousLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreviousLectureRepository extends JpaRepository<PreviousLecture, Long> {
    List<PreviousLecture> findAllByCourseList(List<Course> courseList);

    Optional<PreviousLecture> findBySemesterYearAndCode(String semesterYear, String code);

    List<PreviousLecture> findAllBySemesterYear(String semesterYear);
}
