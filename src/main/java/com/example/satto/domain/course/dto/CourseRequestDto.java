package com.example.satto.domain.course.dto;

public record CourseRequestDto(
        String cmpDiv,
        String name,
        String semesterYear,
        String code
) {
}
