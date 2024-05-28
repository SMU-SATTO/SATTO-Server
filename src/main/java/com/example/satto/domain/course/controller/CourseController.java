package com.example.satto.domain.course.controller;

import com.example.satto.domain.course.converter.CourseConverter;
import com.example.satto.domain.course.converter.PreviousLecturesConverter;
import com.example.satto.domain.course.dto.*;
import com.example.satto.domain.course.entity.PreviousLecture;
import com.example.satto.domain.course.service.CourseService;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.global.annotation.AuthUser;
import com.example.satto.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
@CrossOrigin("*")
@Tag(name = "Course Domain Api", description = "이전 강의, 사용자 수강 목록 관련 Api입니다.")
public class CourseController {

    private final CourseService courseService;

    @Operation(method = "GET", summary = "이전 강의 조회",
            description = "사용자가 수강 목록을 만드는 과정에서 이전 강의 정보를 검색할때 사용됩니다. 요청한 강의 정보를 전송합니다.")
    @GetMapping("/search")
    public BaseResponse<PreviousLectureResponseDto> getPreviousLecture(
            @RequestParam("semester-year") String semesterYear,
            @RequestParam("code") String code) {
        PreviousLecture previousLecture = courseService.getPreviousLecture(semesterYear, code);
        return BaseResponse.onSuccess(PreviousLecturesConverter
                .toPreviousLectureResponseDto(previousLecture));
    }

    @Operation(method = "GET", summary = "(특정 년도학기 요청) 이전 강의 목록 조회",
            description = "사용자가 수강 목록을 만드는 과정에 제공할 해당 년도학기의 강의 정보들을 전송합니다.")
    @GetMapping("")
    public BaseResponse<PreviousLectureListResponseDto> getPreviousLectureList(
            @RequestParam("semester-year") String semesterYear) {
        List<PreviousLecture> previousLectureList = courseService.getPreviousLectureListBySemesterYear(semesterYear);
        return BaseResponse.onSuccess(PreviousLecturesConverter
                .toPreviousLecturesResponseDtoList(previousLectureList));

    }

    @Operation(method = "GET", summary = "사용자 수강 목록 조회",
            description = "사용자가 입력한 수강 목록을 조회합니다. 해당 사용자의 수강 목록에 속한 강의 정보 리스트를 전송합니다.")
    @GetMapping("")
    public BaseResponse<CourseResponseListDto> getCourseList(
            @AuthUser Users user) {
        List<PreviousLecture> previousLectureList = courseService.getCourse(user);
        return BaseResponse.onSuccess(CourseConverter
                .toCourseResponseDtoList(previousLectureList));
    }

    @Operation(method = "POST", summary = "사용자 수강 목록 추가",
            description = "사용자의 수강 목록에 요청된 강의를 추가합니다.")
    @PostMapping("/{email}")
    public BaseResponse<?> updateCourse(
            @PathVariable String email,
            @RequestBody CourseRequestListDto courseRequestListDto) {
        courseService.updateCourse(email, courseRequestListDto);
        return BaseResponse.onSuccess("수정 되었습니다.");
    }

    @Operation(method = "DELETE", summary = "사용자 수강 목록 삭제",
            description = "사용자의 수강 목록에서 요청된 강의를 삭제합니다.")
    @DeleteMapping("/{email}")
    public BaseResponse<?> deleteCourse(
            @PathVariable String email,
            @RequestBody CourseRequestListDto courseRequestListDto) {
        courseService.deleteCourse(email, courseRequestListDto);
        return BaseResponse.onSuccess("삭제 되었습니다.");
    }

    @Operation(method = "GET", summary = "사용자 졸업 요건 충족도 조회",
            description = "사용자의 수강 목록을 기반으로 졸업 요건 충족 학점 정보를 전송합니다.")
    @GetMapping("/graduation")
    public BaseResponse<GraduationResponseDto> getGraduation(
            @AuthUser Users user) {
        List<PreviousLecture> previousLectureList = courseService.getCourse(user);
        return BaseResponse.onSuccess(CourseConverter
                .toGraduationResponseDto(previousLectureList));
    }
}
