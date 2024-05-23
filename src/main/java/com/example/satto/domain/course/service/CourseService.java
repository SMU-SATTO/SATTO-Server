package com.example.satto.domain.course.service;

import com.example.satto.domain.course.dto.CourseRequestListDto;
import com.example.satto.domain.course.entity.Course;
import com.example.satto.domain.course.entity.PreviousLecture;
import com.example.satto.domain.course.repository.CourseRepository;
import com.example.satto.domain.course.repository.PreviousLectureRepository;
import com.example.satto.domain.user.entity.User;
import com.example.satto.domain.user.repository.UserRepository;
import com.example.satto.global.common.code.status.ErrorStatus;
import com.example.satto.global.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PreviousLectureRepository previousLectureRepository;

    @Transactional(readOnly = true)
    public PreviousLecture getPreviousLecture(String semesterYear, String code) {
        return previousLectureRepository.findBySemesterYearAndCode(semesterYear, code)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PREVIOUS_LECTURE));
    }

    @Transactional(readOnly = true)
    public List<PreviousLecture> getPreviousLectureList(String semesterYear) {
        return previousLectureRepository.findAllBySemesterYear(semesterYear);
    }

    @Transactional(readOnly = true)
    public List<PreviousLecture> getPreviousLectureList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorStatus._NOT_FOUND_USER));
        List<Course> courseList = courseRepository.findAllByUser(user);
        return previousLectureRepository.findAllByCourseList(courseList);
    }

    public List<PreviousLecture> updateCourse(Long userId, CourseRequestListDto courseRequestListDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorStatus._NOT_FOUND_USER));
        List<Course> courseList = courseRepository.findAllByUser(user);
        List<PreviousLecture> previousLectureList = previousLectureRepository.findAllByCourseList(courseList);
        return previousLectureList;
    }

    public void deleteCourse(Long userId, CourseRequestListDto courseRequestListDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorStatus._NOT_FOUND_USER));
        List<Course> courseList = courseRepository.findAllByUser(user);

    }
}
