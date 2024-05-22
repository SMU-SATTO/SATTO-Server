package com.example.satto.domain.course.controller;

import com.example.satto.domain.course.converter.CourseConverter;
import com.example.satto.domain.course.converter.PreviousLecturesConverter;
import com.example.satto.domain.course.dto.CourseResponseListDto;
import com.example.satto.domain.course.dto.PreviousLectureListResponseDto;
import com.example.satto.domain.course.dto.PreviousLectureResponseDto;
import com.example.satto.domain.course.entity.PreviousLecture;
import com.example.satto.domain.course.service.CourseService;
import com.example.satto.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
@CrossOrigin("*")
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
        List<PreviousLecture> previousLectureList = courseService.getPreviousLectureList(semesterYear);
        return BaseResponse.onSuccess(PreviousLecturesConverter
                .toPreviousLecturesResponseDtoList(previousLectureList));

    }

    @Operation(method = "GET", summary = "사용자 수강 목록 조회",
            description = "사용자가 입력한 수강 목록을 조회합니다. 해당 사용자의 수강 목록에 속한 강의 정보 리스트를 전송합니다.")
    @GetMapping("/{courseId}")
    public BaseResponse<CourseResponseListDto> getCourseList(
            @PathVariable Long courseId) {
        List<PreviousLecture> previousLectureList = courseService.getPreviousLectureList(courseId);
        return BaseResponse.onSuccess(CourseConverter
                .toCourseResponseDtoList(previousLectureList));
    }

}
