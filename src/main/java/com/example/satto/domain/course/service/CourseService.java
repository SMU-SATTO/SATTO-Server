package com.example.satto.domain.course.service;

import com.example.satto.domain.course.entity.PreviousLecture;
import com.example.satto.domain.course.repository.CourseRepository;
import com.example.satto.domain.course.repository.PreviousLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final PreviousLectureRepository previousLectureRepository;

    public PreviousLecture getPreviousLecture(String semesterYear, String code) {

    }

    public List<PreviousLecture> getPreviousLectureList(String semesterYear) {

    }

    public List<PreviousLecture> getPreviousLectureList(Long courseId) {

    }
}
