package com.example.satto.domain.course.service;

import com.example.satto.domain.course.dto.CourseRequestDto;
import com.example.satto.domain.course.dto.GraduationRequestDto;
import com.example.satto.domain.course.dto.SearchCourseRequestDto;
import com.example.satto.domain.course.repository.CourseRepository;
import com.example.satto.domain.course.repository.PreviousLectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final PreviousLectureRepository previousLectureRepository;

    public void searchCourse(SearchCourseRequestDto request) {

    }

    public void getCoures(CourseRequestDto request, Long userId) {

    }

    public void updateCourse(CourseRequestDto request, Long userId) {

    }

    public void deleteCoures(CourseRequestDto request, Long userId) {

    }

    public void getGraduation(GraduationRequestDto request, Long userId) {

    }
}
