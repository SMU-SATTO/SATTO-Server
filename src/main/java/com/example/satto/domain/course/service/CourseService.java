package com.example.satto.domain.course.service;

import com.example.satto.domain.course.dto.CourseRequestDto;
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
    public List<PreviousLecture> getPreviousLectureListBySemesterYear(String semesterYear) {
        return previousLectureRepository.findAllBySemesterYear(semesterYear);
    }

    //사용자 수강 목록 조회
    @Transactional(readOnly = true)
    public List<PreviousLecture> getCourse(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new GeneralException(ErrorStatus._NOT_FOUND_USER));
        List<Course> courseList = courseRepository.findAllByUser(user);
        return previousLectureRepository.findAllByCourseList(courseList);
    }

    //사용자 수강 목록 수정(추가)
    public void updateCourse(String email, CourseRequestListDto courseRequestListDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_USER));
        for (CourseRequestDto courseRequestDto : courseRequestListDto.courseRequestDtoList()) {
            PreviousLecture previousLecture = previousLectureRepository.findBySemesterYearAndCode(courseRequestDto.semesterYear(), courseRequestDto.code())
                    .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PREVIOUS_LECTURE));
            Course course = Course.builder()
                    .user(user)
                    .previousLecture(previousLecture)
                    .build();
            courseRepository.save(course);
        }
    }


    //사용자 수강 목록 수정(삭제)
    public void deleteCourse(String email, CourseRequestListDto courseRequestListDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_USER));
        for (CourseRequestDto courseRequestDto : courseRequestListDto.courseRequestDtoList()) {
            PreviousLecture previousLecture = previousLectureRepository.findBySemesterYearAndCode(courseRequestDto.semesterYear(), courseRequestDto.code())
                    .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PREVIOUS_LECTURE));
            Course course = courseRepository.findByUserAndPreviousLecture(user, previousLecture)
                    .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_COURSE));
            courseRepository.delete(course);
        }
    }
}
