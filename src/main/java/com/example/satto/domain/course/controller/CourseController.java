package com.example.satto.domain.course.controller;

import com.example.satto.domain.course.dto.CourseRequestDto;
import com.example.satto.domain.course.dto.CourseResponseDto;
import com.example.satto.domain.course.dto.GraduationRequestDto;
import com.example.satto.domain.course.dto.SearchCourseRequestDto;
import com.example.satto.domain.course.service.CourseService;
import com.example.satto.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
@RestController
public class CourseController {

    private final CourseService courseService;

    //이전 강의 조회
    @GetMapping("/search")
    public BaseResponse<CourseResponseDto> searchCourse(
            @RequestBody SearchCourseRequestDto request) {
        courseService.searchCourse(request);
        return BaseResponse.onSuccess();
    }

    //사용자 수강 목록 조회
    @GetMapping("/{courseId}")
    public BaseResponse<CourseResponseDto> getCourse(
            @RequestBody CourseRequestDto request,
            @PathVariable Long courseId) {
        courseService.getCoures(request, courseId);
        return BaseResponse.onSuccess();
    }

    //사용자 수강 목록 추가
    @PostMapping("/{courseId}")
    public BaseResponse<CourseResponseDto> updateCourse
    (@RequestBody CourseRequestDto request,
     @PathVariable Long courseId) {
        courseService.updateCourse(request, courseId);
        return BaseResponse.onSuccess();
    }

    //사용자 수강 목록 삭제
    @DeleteMapping("/{courseId}")
    public BaseResponse<CourseResponseDto> deleteCourse(
            @RequestBody CourseRequestDto request,
            @PathVariable Long courseId) {
        courseService.deleteCoures(request, courseId);
        return BaseResponse.onSuccess("삭제되었습니다.");
    }

    //사용자 졸업 요건 충족도 조회
    @GetMapping("/{courseId}/graduation")
    public BaseResponse<CourseResponseDto> getGraduation(
            @RequestBody GraduationRequestDto request,
            @PathVariable Long courseId) {
        courseService.getGraduation(request, courseId);
        return BaseResponse.onSuccess();
    }
}
