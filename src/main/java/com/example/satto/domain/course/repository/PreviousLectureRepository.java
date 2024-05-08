package com.example.satto.domain.course.repository;

import com.example.satto.domain.course.entity.PreviousLectures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreviousLectureRepository extends JpaRepository<PreviousLectures, Long> {
}
