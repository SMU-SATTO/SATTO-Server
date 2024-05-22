package com.example.satto.domain.course.dto;

import java.util.List;

public record PostCourseRequestDto(
        List<CourseRequestDto> courseRequestDtoList
) {

}
