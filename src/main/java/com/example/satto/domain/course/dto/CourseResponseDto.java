package com.example.satto.domain.course.dto;

public record CourseResponseDto(
        String cmpDiv,
        String name,
        String professor,
        String semesterYear,
        String code
) {
}
