package com.example.satto.domain.course.dto;

import java.util.List;

public record CourseListResponseDto(
        List<CourseResponseDto> courseResponseDtoList
) {
}
