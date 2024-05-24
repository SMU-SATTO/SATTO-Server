package com.example.satto.domain.course.dto;

import lombok.Builder;

@Builder
public record CourseResponseDto(
        String cmpDiv,
        String name,
        String professor,
        String semesterYear,
        String code
) {
}
