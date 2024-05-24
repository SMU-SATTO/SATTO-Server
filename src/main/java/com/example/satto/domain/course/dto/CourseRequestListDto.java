package com.example.satto.domain.course.dto;

import java.util.List;

public record CourseRequestListDto(
        List<CourseRequestDto> courseRequestDtoList
) {

}
